package com.smartops.smartserve.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.smartops.smartserve.constants.SSTableStatus;
import com.smartops.smartserve.mapper.SSCommonListMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SSTableState extends SSDomain {

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

	@Column(nullable = true, columnDefinition = "nvarchar(max)")
	@Convert(converter = SSCommonListMapper.class)
	private List<String> lastEvent;

	private double lastConfidence;

}
