package com.ssafy.wattagatta.domain.agent.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class WebSocketSessionManager {

    private WebSocketSession webSocketSession;
    private final Map<String, Set<WebSocketSession>> sessionMap = new HashMap<>();

    public void addSession(String path, WebSocketSession session) {
        sessionMap.computeIfAbsent(path, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
    }

    public void removeSession(String path, WebSocketSession session) {
        Set<WebSocketSession> sessions = sessionMap.get(path);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionMap.remove(path);
            }
        }
    }

    public boolean sendMessageToPath(String path, String message) {
        Set<WebSocketSession> sessions = sessionMap.get(path);
        if (sessions == null || sessions.isEmpty()) {
//            log.warn("No active WebSocket sessions for path: {}", path);
            return false;
        }

        boolean success = true;
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    log.error("Failed to send message to session: {}", session.getId(), e);
                    success = false;
                }
            }
        }
        return success;
    }

    public Set<WebSocketSession> getSessionsForPath(String path) {
        return sessionMap.getOrDefault(path, Collections.emptySet());
    }

}
