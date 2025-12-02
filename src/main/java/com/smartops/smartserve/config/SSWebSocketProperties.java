package com.smartops.smartserve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "ss.realtime")
@Getter
@Setter
public class SSWebSocketProperties {
	/**
	 * Comma-separated allowed origins for WebSocket (e.g.
	 * http://localhost:4200,http://example.com)
	 */
	private String allowedOrigins = "*";
}
