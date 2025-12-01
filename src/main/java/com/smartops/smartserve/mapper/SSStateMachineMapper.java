package com.smartops.smartserve.mapper;

import com.smartops.smartserve.constants.SSCameraEvent;
import com.smartops.smartserve.constants.SSCustomerEvent;
import com.smartops.smartserve.constants.SSServiceEvent;
import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.constants.SSWaiterAction;
import com.smartops.smartserve.domain.SSTableState;

public class SSStateMachineMapper {

	public static SSTableStatus processEvent(SSTableState state, String eventRaw) {

		SSTableStatus current = state.getTableStatus();

		// Try Camera Events
		try {
			SSCameraEvent ce = SSCameraEvent.valueOf(eventRaw);
			return SSCameraEventMapper.toStatus(ce, current);
		} catch (Exception ignored) {
		}

		// Try Customer Events
		try {
			SSCustomerEvent ce = SSCustomerEvent.valueOf(eventRaw);
			return SSCustomerEventMapper.toStatus(ce, current);
		} catch (Exception ignored) {
		}

		// Try Waiter Actions
		try {
			SSWaiterAction wa = SSWaiterAction.valueOf(eventRaw);
			return SSWaiterActionMapper.toStatus(wa, current);
		} catch (Exception ignored) {
		}

		// Try Service Events (optional)
		try {
			SSServiceEvent se = SSServiceEvent.valueOf(eventRaw);
			return current; // service events don’t change status
		} catch (Exception ignored) {
		}

		return current; // default
	}
}