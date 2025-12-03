package com.smartops.smartserve.mapper;

import com.smartops.smartserve.constants.SSCameraEvent;
import com.smartops.smartserve.constants.SSTableStatus;

@Deprecated
public class SSCameraEventMapper {

	public static SSTableStatus toStatus(SSCameraEvent event, SSTableStatus current) {

		return switch (event) {

		case PERSON_DETECTED -> SSTableStatus.OCCUPIED;

		case GLASS_DETECTED -> SSTableStatus.ACTIVE;
		case FOOD_DETECTED -> SSTableStatus.ACTIVE;
		case PLATE_DETECTED -> SSTableStatus.ACTIVE;

		case GLASS_EMPTY -> SSTableStatus.NEEDS_WATER;
		case FOOD_FINISHED -> SSTableStatus.FOOD_FINISHED_STATE;

		default -> current != null ? current : SSTableStatus.UNKNOWN;
		};
	}
}