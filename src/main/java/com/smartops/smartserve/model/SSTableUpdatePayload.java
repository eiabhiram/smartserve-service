package com.smartops.smartserve.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SSTableUpdatePayload {
	
	private Long tableId;
	
    private String status;
    
    private String lastEvent;

}
