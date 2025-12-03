package com.smartops.smartserve.model;

import java.util.List;

import com.smartops.smartserve.constants.SSCameraEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSCameraEventRequest {

	private Long tableId;

	private List<SSCameraEvent> actions;

	private double confidence;

}
