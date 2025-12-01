package com.smartops.smartserve.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSCameraRequestEvent {
	
	private Integer tableId;
	
    private String event;  
    
    private double confidence;

}
