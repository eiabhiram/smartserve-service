package com.smartops.smartserve.mapper;

import com.smartops.smartserve.constants.SSCustomerEvent;
import com.smartops.smartserve.constants.SSTableStatus;

public class SSCustomerEventMapper {

	public static SSTableStatus toStatus(SSCustomerEvent event, SSTableStatus current) {

		return switch (event) {

		case CALL_WAITER -> current;

		case REQUEST_WATER -> current;

		default -> current;
		};
	}
}