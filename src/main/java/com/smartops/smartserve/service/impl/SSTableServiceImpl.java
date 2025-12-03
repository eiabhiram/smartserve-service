package com.smartops.smartserve.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartops.smartserve.constants.SSCameraEvent;
import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.domain.SSEventsLog;
import com.smartops.smartserve.domain.SSTableState;
import com.smartops.smartserve.events.realtime.SSRealtimeOperation;
import com.smartops.smartserve.events.realtime.SSRealtimeUpdate;
import com.smartops.smartserve.exception.SSBusinessException;
import com.smartops.smartserve.mapper.SSStateMachineMapper;
import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSCustomerEventRequest;
import com.smartops.smartserve.model.SSErrorModelDTO;
import com.smartops.smartserve.model.SSTableStateDTO;
import com.smartops.smartserve.model.SSWaiterActionRequest;
import com.smartops.smartserve.model.ws.SSTableUpdatePayload;
import com.smartops.smartserve.repository.SSEventLogRepository;
import com.smartops.smartserve.repository.SSTableStateRepository;
import com.smartops.smartserve.service.SSTableService;
import com.smartops.smartserve.service.validation.SSCameraEventValidationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SSTableServiceImpl implements SSTableService {

	private final SSTableStateRepository tableRepo;
	private final SSEventLogRepository eventRepo;
	private final SSCameraEventValidationService validationService;

	@Transactional
	@Override
	@SSRealtimeUpdate(topic = "table", operation = SSRealtimeOperation.CREATE)
	public SSTableUpdatePayload createTable(SSTableStateDTO ssTableStateDTO) {
		SSTableState state = new SSTableState();
		state.setTableStatus(SSTableStatus.FREE); // default state
		state.setTableName(ssTableStateDTO.getTableName());
		state.setChairCount(ssTableStateDTO.getChairCount());
		state.setIsActive(true);
		state.setIsReserved(false);

		SSTableState saved = tableRepo.save(state);

		// log single create event as a list of one item
		logEvents(saved.getId(), List.of("TABLE_CREATED"), 0d);

		return new SSTableUpdatePayload(saved.getId(), saved.getTableStatus().name(), saved.getLastEvent());
	}

	@Transactional
	@Override
	@SSRealtimeUpdate(topic = "table", operation = SSRealtimeOperation.UPDATE)
	public SSTableUpdatePayload processCameraEvent(SSCameraEventRequest req) {

		String currentStatus = getTableStatus(req.getTableId());
		SSErrorModelDTO validationError = validationService.validateCameraEvent(req, currentStatus);

		if (validationError != null) {
			throw new SSBusinessException(validationError.getValidationMessage(), validationError.getArgs());
		}

		// process multiple signals sent in one request
		return processMultipleEvents(req.getTableId(), req.getActions(), req.getConfidence());
	}

	@Transactional
	@Override
	@SSRealtimeUpdate(topic = "table", operation = SSRealtimeOperation.UPDATE)
	public SSTableUpdatePayload processCustomerEvent(SSCustomerEventRequest req) {
		return processEvent(req.getTableId(), req.getAction(), 1.0);
	}

	@Transactional
	@Override
	@SSRealtimeUpdate(topic = "table", operation = SSRealtimeOperation.UPDATE)
	public SSTableUpdatePayload processWaiterAction(SSWaiterActionRequest req) {
		return processEvent(req.getTableId(), req.getAction(), 1.0);
	}

	@Override
	public List<SSTableState> getAll() {
		return tableRepo.findAll();
	}

	@Override
	public Optional<SSTableState> get(Long tableId) {
		return tableRepo.findById(tableId);
	}

	@Override
	public String getTableStatus(Long tableId) {
		return tableRepo.findById(tableId).map(SSTableState::getTableStatus).map(Enum::name).orElse("UNKNOWN");
	}

	private SSTableUpdatePayload processEvent(Long tableId, String event, double confidence) {
		if (tableId == null || event == null)
			return null;

		SSTableState state = tableRepo.findById(tableId).orElse(null);
		if (state == null)
			return null;

		// log single event as a list
		logEvents(tableId, List.of(event), confidence);

		SSTableStatus newStatus = SSStateMachineMapper.processEvent(state, event);
		updateState(state, newStatus, List.of(event), confidence);

		return new SSTableUpdatePayload(state.getId(), state.getTableStatus().name(), state.getLastEvent());
	}

	private SSTableUpdatePayload processMultipleEvents(Long tableId, List<SSCameraEvent> events, double confidence) {
		if (tableId == null || events == null || events.isEmpty())
			return null;

		SSTableState state = tableRepo.findById(tableId).orElse(null);
		if (state == null)
			return null;

		// 1) Log all signals as a single DB row (events list)
		List<String> eventNames = events.stream().map(Enum::name).collect(Collectors.toList());
		logEvents(tableId, eventNames, confidence);

		// 2) Compute final status
		SSTableStatus finalStatus = computeFinalStatus(state, events);

		// 3) Update table state (use the first event as lastEvent for trace)
		updateState(state, finalStatus, eventNames.isEmpty() ? null : eventNames, confidence);

		// 4) Return payload (WebSocket aspect will publish using @SSRealtimeUpdate)
		return new SSTableUpdatePayload(state.getId(), state.getTableStatus().name(), state.getLastEvent());
	}

	/**
	 * computeFinalStatus(...) as you designed earlier (stateful state machine). Use
	 * SSStateMachineMapper if you prefer to centralize mapping logic there.
	 */
	private SSTableStatus computeFinalStatus(SSTableState state, List<SSCameraEvent> events) {

		SSTableStatus current = state.getTableStatus();

		boolean person = events.contains(SSCameraEvent.PERSON_DETECTED);
		boolean personLeft = events.contains(SSCameraEvent.PERSON_NOT_DETECTED);

		boolean plate = events.contains(SSCameraEvent.PLATE_DETECTED);
		boolean food = events.contains(SSCameraEvent.FOOD_DETECTED);
		boolean glass = events.contains(SSCameraEvent.GLASS_DETECTED);

		boolean glassEmpty = events.contains(SSCameraEvent.GLASS_EMPTY);
		boolean foodFinished = events.contains(SSCameraEvent.FOOD_FINISHED);

		// 1) FREE -> OCCUPIED
		if (person && current == SSTableStatus.FREE) {
			return SSTableStatus.OCCUPIED;
		}

		// 2) OCCUPIED -> ACTIVE (need at least one dining item)
		if (current == SSTableStatus.OCCUPIED && (plate || food || glass)) {
			return SSTableStatus.ACTIVE;
		}

		// 3) ACTIVE -> NEEDS_WATER
		if (current == SSTableStatus.ACTIVE && glassEmpty && !foodFinished) {
			return SSTableStatus.NEEDS_WATER;
		}

		// 4) ACTIVE -> FOOD_FINISHED_STATE
		if (current == SSTableStatus.ACTIVE && foodFinished) {
			return SSTableStatus.FOOD_FINISHED_STATE;
		}

		// 5) FOOD_FINISHED_STATE -> NEEDS_CLEANING (person left)
		if (current == SSTableStatus.FOOD_FINISHED_STATE && personLeft) {
			return SSTableStatus.NEEDS_CLEANING;
		}

		// 6) NEEDS_CLEANING -> (camera cannot move to FREE; waiter marks cleaned)
		if (current == SSTableStatus.NEEDS_CLEANING && personLeft) {
			return SSTableStatus.NEEDS_CLEANING;
		}

		// default: no change
		return current;
	}

	private void updateState(SSTableState state, SSTableStatus newStatus, List<String> event, double confidence) {
		state.setTableStatus(newStatus);
		state.setLastEvent(event);
		state.setLastConfidence(confidence);
		tableRepo.save(state);
	}

	/**
	 * Save one SSEventsLog row containing all events for this request.
	 */
	private void logEvents(Long tableId, List<String> events, double confidence) {
		SSEventsLog log = new SSEventsLog();
		log.setTableId(tableId);
		log.setEvents(events);
		log.setConfidence(confidence);
		eventRepo.save(log);
	}
}
