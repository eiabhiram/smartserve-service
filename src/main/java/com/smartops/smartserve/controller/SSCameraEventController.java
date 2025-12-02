package com.smartops.smartserve.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartops.smartserve.model.SSCameraEventRequest;
import com.smartops.smartserve.service.SSTableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events/camera")
@RequiredArgsConstructor
public class SSCameraEventController {

    private final SSTableService tableService;

    @PostMapping
    public ResponseEntity<String> receiveCameraEvent(@RequestBody SSCameraEventRequest req) {
        tableService.processCameraEvent(req);
        return ResponseEntity.ok("camera event processed");
    }
}
