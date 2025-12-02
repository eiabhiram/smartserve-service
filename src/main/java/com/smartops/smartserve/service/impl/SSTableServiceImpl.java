package com.smartops.smartserve.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.domain.SSEventsLog;
import com.smartops.smartserve.domain.SSTableState;
import com.smartops.smartserve.events.realtime.SSRealtimeOperation;
import com.smartops.smartserve.events.realtime.SSRealtimeUpdate;
import com.smartops.smartserve.mapper.SSStateMachineMapper;
import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSCustomerEventRequest;
import com.smartops.smartserve.model.SSTableStateDTO;
import com.smartops.smartserve.model.SSWaiterActionRequest;
import com.smartops.smartserve.model.ws.SSTableUpdatePayload;
import com.smartops.smartserve.repository.SSEventLogRepository;
import com.smartops.smartserve.repository.SSTableStateRepository;
import com.smartops.smartserve.service.SSTableService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SSTableServiceImpl implements SSTableService {

	private final SSTableStateRepository tableRepo;
	private final SSEventLogRepository eventRepo;

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
		state.setTableName(ssTableStateDTO.getTableName());
		state.setTableName(ssTableStateDTO.getTableName());
		SSTableState saved = tableRepo.save(state);
		logEvent(saved.getId(), "TABLE_CREATED", 0);
		return new SSTableUpdatePayload(saved.getId(), saved.getTableStatus().name(), saved.getLastEvent());
	}

	@Transactional
	@Override
	@SSRealtimeUpdate(topic = "table", operation = SSRealtimeOperation.UPDATE)
	public SSTableUpdatePayload processCameraEvent(SSCameraEventRequest req) {
		return processEvent(req.getTableId(), req.getAction(), req.getConfidence());
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

	private SSTableUpdatePayload processEvent(Long tableId, String event, double confidence) {

		if (tableId == null || event == null)
			return null;

		SSTableState state = tableRepo.findById(tableId).get();
		logEvent(tableId, event, confidence);
		SSTableStatus newStatus = SSStateMachineMapper.processEvent(state, event);
		updateState(state, newStatus, event, confidence);
		return new SSTableUpdatePayload(state.getId(), state.getTableStatus().name(), state.getLastEvent());
	}

	private void updateState(SSTableState state, SSTableStatus newStatus, String event, double confidence) {
		state.setTableStatus(newStatus);
		state.setLastEvent(event);
		state.setLastConfidence(confidence);
		tableRepo.save(state);
	}

	private void logEvent(Long tableId, String event, double confidence) {
		SSEventsLog log = new SSEventsLog();
		log.setTableId(tableId);
		log.setEvent(event);
		log.setConfidence(confidence);
		eventRepo.save(log);
	}
}
