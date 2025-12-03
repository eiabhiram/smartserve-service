package com.smartops.smartserve.mapper;

import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.constants.SSWaiterAction;

public class SSWaiterActionMapper {

	public static SSTableStatus toStatus(SSWaiterAction action, SSTableStatus current) {

		return switch (action) {

		case MARK_WATER_SERVED -> SSTableStatus.ACTIVE;

		case MARK_TABLE_CLEANED -> SSTableStatus.FREE;

		default -> current;
		};
	}
}