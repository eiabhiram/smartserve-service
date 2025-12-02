package com.smartops.smartserve.model.ws;

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
