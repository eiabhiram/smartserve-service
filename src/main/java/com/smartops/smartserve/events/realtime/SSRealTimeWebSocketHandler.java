package com.smartops.smartserve.events.realtime;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import tools.jackson.databind.ObjectMapper;

/**
 * Maintains active sessions and provides methods for broadcasting structured
 * JSON messages. Stateless and thread-safe.
 */
@Component
public class SSRealTimeWebSocketHandler extends TextWebSocketHandler {

	private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
	private final ConcurrentHashMap<String, Object> sessionMeta = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		// Optionally capture metadata from handshake attributes:
		// Object user = session.getAttributes().get("user");
		// sessionMeta.put(session.getId(), user);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// If you need to support client messages, parse them here.
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
		sessions.remove(session);
		sessionMeta.remove(session.getId());
	}

	/**
	 * Broadcast a RealtimeMessage to all open sessions.
	 */
	public void broadcast(SSRealtimeMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			TextMessage tm = new TextMessage(json);
			for (WebSocketSession s : sessions) {
				if (s.isOpen()) {
					try {
						s.sendMessage(tm);
					} catch (IOException e) {
						// log and continue
					}
				}
			}
		} catch (Exception e) {
			// log
		}
	}

	/**
	 * Send to a single session id, if you need targeted push in future.
	 */
	public void sendToSession(String sessionId, SSRealtimeMessage message) {
		sessions.stream().filter(s -> s.getId().equals(sessionId) && s.isOpen()).findFirst().ifPresent(s -> {
			try {
				s.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
			} catch (IOException e) {
				/* log */ }
		});
	}
}
