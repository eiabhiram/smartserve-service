package com.smartops.smartserve.events.realtime.service.impl;

import org.springframework.stereotype.Service;

import com.smartops.smartserve.events.realtime.SSRealTimeWebSocketHandler;
import com.smartops.smartserve.events.realtime.SSRealtimeMessage;
import com.smartops.smartserve.events.realtime.service.SSRealTimeNotificationService;

import lombok.RequiredArgsConstructor;

/**
 * Service used by your application services (TableService etc.) to publish
 * realtime updates.
 */
@Service
@RequiredArgsConstructor
public class SSRealTimeNotificationServiceImpl implements SSRealTimeNotificationService {

	private final SSRealTimeWebSocketHandler handler;

	@Override
	public void publish(SSRealtimeMessage message) {
		handler.broadcast(message);
	}
}
