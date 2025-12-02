package com.smartops.smartserve.events.realtime.service;

import com.smartops.smartserve.events.realtime.SSRealtimeMessage;

public interface SSRealTimeNotificationService {

	void publish(SSRealtimeMessage message);

}
