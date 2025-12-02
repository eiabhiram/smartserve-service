package com.smartops.smartserve.config;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Handshake interceptor that allows you to plug-in auth logic easily. Currently
 * it's a pass-through; to enable JWT check, add logic in beforeHandshake.
 */
public class SSCustomHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
			@NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

		// Example hook:
		// - extract Authorization header
		// - validate token via JwtDecoder
		// - put user details into `attributes` (e.g. attributes.put("user", userDto))

		// For now, allow the connection.
		return true;
	}

	@Override
	public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
			@NonNull WebSocketHandler wsHandler, Exception exception) {
		// noop
	}
}
