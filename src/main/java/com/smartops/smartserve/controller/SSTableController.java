package com.smartops.smartserve.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartops.smartserve.domain.SSTableState;
import com.smartops.smartserve.model.SSTableActionRequest;
import com.smartops.smartserve.model.SSWaiterActionRequest;
import com.smartops.smartserve.service.SSTableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class SSTableController {

	private final SSTableService tableService;

	@GetMapping
	public List<SSTableState> allTables() {
		return tableService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<SSTableState> getTable(@PathVariable Long id) {
		return tableService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Manual action endpoint for UI (e.g., mark cleaned, mark free). Accepts
	 * payload like { "action": "MARK_TABLE_CLEANED" }
	 */
	@PostMapping("/{id}/action")
	public ResponseEntity<String> performAction(@PathVariable Long id,
			@RequestBody SSTableActionRequest actionRequest) {

		// convert to WaiterActionRequest shape and reuse service
		SSWaiterActionRequest req = new SSWaiterActionRequest();
		req.setTableId(id);
		req.setAction(actionRequest.getAction());

		tableService.processWaiterAction(req);
		return ResponseEntity.ok("action applied");
	}
}