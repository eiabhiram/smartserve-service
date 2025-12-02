package com.smartops.smartserve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.smartops.smartserve.events.realtime.SSRealTimeWebSocketHandler;

@Configuration
@EnableWebSocket
public class SSWebSocketConfig implements WebSocketConfigurer {

	private final SSRealTimeWebSocketHandler handler;
	private final SSWebSocketProperties properties;

	public SSWebSocketConfig(SSRealTimeWebSocketHandler handler, SSWebSocketProperties properties) {
		this.handler = handler;
		this.properties = properties;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		String[] origins = properties.getAllowedOrigins() == null ? new String[] { "*" }
				: properties.getAllowedOrigins().split(",");

		registry.addHandler(handler, "/api/realtime/events").setAllowedOrigins(origins)
				.addInterceptors(new SSCustomHandshakeInterceptor());
	}
}
