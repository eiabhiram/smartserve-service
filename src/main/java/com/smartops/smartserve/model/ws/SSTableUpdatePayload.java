package com.smartops.smartserve.model.ws;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SSTableUpdatePayload {

	private Long tableId;

	private String status;

	private List<String> lastEvent;

}
