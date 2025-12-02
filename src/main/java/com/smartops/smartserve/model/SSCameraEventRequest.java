package com.smartops.smartserve.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSCameraEventRequest {
	
	private Long tableId;
	
    private String event;  
    
    private double confidence;

}
