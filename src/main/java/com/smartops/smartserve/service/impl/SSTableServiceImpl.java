package com.smartops.smartserve.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.domain.SSEventsLog;
import com.smartops.smartserve.domain.SSTableState;
import com.smartops.smartserve.events.realtime.SSRealtimeMessage;
import com.smartops.smartserve.events.realtime.SSRealtimeOperation;
import com.smartops.smartserve.events.realtime.service.SSRealTimeNotificationService;
import com.smartops.smartserve.mapper.SSStateMachineMapper;
import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.model.SSCustomerEventRequest;
import com.smartops.smartserve.model.SSTableUpdatePayload;
import com.smartops.smartserve.model.SSWaiterActionRequest;
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
	private final SSRealTimeNotificationService notifService;

	@Transactional
	@Override
	public void processCameraEvent(SSCameraEventRequest req) {
		processEvent(req.getTableId(), req.getEvent(), req.getConfidence());
	}

	@Transactional
	@Override
	public void processCustomerEvent(SSCustomerEventRequest req) {
		processEvent(req.getTableId(), req.getEvent(), 1.0);
	}

	@Transactional
	@Override
	public void processWaiterAction(SSWaiterActionRequest req) {
		processEvent(req.getTableId(), req.getAction(), 1.0);
	}

	@Override
	public List<SSTableState> getAll() {
		return tableRepo.findAll();
	}

	@Override
	public Optional<SSTableState> get(Long tableId) {
		return tableRepo.findById(tableId);
	}

	private void processEvent(Long tableId, String event, double confidence) {
		if (tableId == null || event == null)
			return;

		SSTableState state = findOrCreate(tableId);

		// 1. Log event
		logEvent(tableId, event, confidence);

		// 2. Apply state change
		SSTableStatus newStatus = SSStateMachineMapper.processEvent(state, event);
		updateState(state, newStatus, event, confidence);

		// 3. Send WebSocket update
		publishRealtimeUpdate(state);
	}

	private SSTableState findOrCreate(Long tableId) {
		return tableRepo.findById(tableId).orElseGet(() -> createNewTableState(tableId));
	}

	private SSTableState createNewTableState(Long tableId) {
		SSTableState ts = new SSTableState();
		ts.setId(tableId);
		ts.setTableStatus(SSTableStatus.FREE);
		return tableRepo.save(ts);
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

	private void publishRealtimeUpdate(SSTableState state) {
		SSTableUpdatePayload payload = new SSTableUpdatePayload(state.getId(), state.getTableStatus().name(),
				state.getLastEvent());

		SSRealtimeMessage message = new SSRealtimeMessage("TABLE_UPDATE", "table", SSRealtimeOperation.UPDATED.name(),
				payload, LocalDateTime.now());

		notifService.publish(message);
	}
}
