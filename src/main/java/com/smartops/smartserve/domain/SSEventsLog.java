package com.smartops.smartserve.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SSEventsLog extends SSDomain {

	private Integer tableId;

	private String event; // raw event (CameraEvent / CustomerEvent / WaiterAction)

	private double confidence; // optional for YOLO

}
