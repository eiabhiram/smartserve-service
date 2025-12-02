package com.smartops.smartserve.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSTableActionRequest {

	private Long tableId;

	private String action;
	
}
