package com.smartops.smartserve.model.ws;

import com.smartops.smartserve.constants.SSTableStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSTableUpdateMessage {
	
	private Integer tableId;
	
    private SSTableStatus status;
    
    private String lastEvent;

}
