package com.smartops.smartserve.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SSEventsLog extends SSDomain {

	private Long tableId;

	private String event;

	private double confidence;

}
