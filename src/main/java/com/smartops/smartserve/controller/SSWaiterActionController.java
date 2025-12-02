package com.smartops.smartserve.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartops.smartserve.model.SSWaiterActionRequest;
import com.smartops.smartserve.service.SSTableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events/waiter")
@RequiredArgsConstructor
public class SSWaiterActionController {

	private final SSTableService tableService;

	@PostMapping
	public ResponseEntity<String> receiveWaiterAction(@RequestBody SSWaiterActionRequest req) {
		tableService.processWaiterAction(req);
		return ResponseEntity.ok("Waiter action processed");
	}
}
