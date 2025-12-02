package com.smartops.smartserve.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartops.smartserve.model.SSCustomerEventRequest;
import com.smartops.smartserve.service.SSTableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events/customer")
@RequiredArgsConstructor
public class SSCustomerEventController {

	private final SSTableService tableService;

	@PostMapping
	public ResponseEntity<String> receiveCustomerEvent(@RequestBody SSCustomerEventRequest req) {
		tableService.processCustomerEvent(req);
		return ResponseEntity.ok("customer event processed");
	}
}
