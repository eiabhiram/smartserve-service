package com.smartops.smartserve.model;

import java.time.LocalDateTime;

import com.smartops.smartserve.constants.SSTableStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSTableStateDTO {

	private String tableName;

	private Boolean isActive;

	private String chairCount;

	private Boolean isReserved;

	private String curentWaiter;

	private String lastWaiter;

	private String sessionId;

	private LocalDateTime currentSessionStartTime;

	@Enumerated(EnumType.STRING)
	private SSTableStatus tableStatus;

	private String lastEvent;

	private double lastConfidence;

}
