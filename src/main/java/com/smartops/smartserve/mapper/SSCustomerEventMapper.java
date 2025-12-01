package com.smartops.smartserve.mapper;

import com.smartops.smartserve.constants.SSCustomerEvent;
import com.smartops.smartserve.constants.SSTableStatus;

public class SSCustomerEventMapper {

	public static SSTableStatus toStatus(SSCustomerEvent event, SSTableStatus current) {

		return switch (event) {

		case CALL_WAITER -> current; // waiter gets notification, state unchanged

		case REQUEST_WATER -> SSTableStatus.NEEDS_WATER;

		case REQUEST_BILL -> SSTableStatus.FOOD_FINISHED_STATE;

		default -> current;
		};
	}
}