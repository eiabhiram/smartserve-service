package com.smartops.smartserve.domain;

import java.util.List;

import com.smartops.smartserve.mapper.SSCommonListMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SSEventsLog extends SSDomain {

	private Long tableId;

	@Column(nullable = true, columnDefinition = "nvarchar(max)")
	@Convert(converter = SSCommonListMapper.class)
	private List<String> events;

	private double confidence;

}
