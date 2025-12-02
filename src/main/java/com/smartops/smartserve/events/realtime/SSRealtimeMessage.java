package com.smartops.smartserve.events.realtime;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Standard JSON message sent to WebSocket clients.
 *
 * Example JSON:
 * {
 *   "type": "TABLE_UPDATE",
 *   "topic": "table",
 *   "operation": "UPDATED",
 *   "payload": {
 *      "tableId": 3,
 *      "status": "NEEDS_WATER",
 *      "lastEvent": "GLASS_EMPTY"
 *   },
 *   "timestamp": "2025-12-02T12:00:00"
 * }
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SSRealtimeMessage {

    // e.g. "TABLE_UPDATE", "NOTIFICATION", "SYSTEM"
    private String type;

    // e.g. "table", "order", "system"
    private String topic;

    // e.g. CREATED, UPDATED, DELETED
    private String operation;

    // any POJO or Map — payload is serialized to JSON
    private Object payload;

    private LocalDateTime timestamp;
}
