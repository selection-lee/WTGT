package com.ssafy.wattagatta.domain.agent.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentDataResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentPositionResponse;
import com.ssafy.wattagatta.domain.agent.manager.WebSocketSessionManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgentWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager webSocketSessionManager;
    private final Map<String, ScheduledExecutorService> schedulerMap = new ConcurrentHashMap<>();

    @Value("${websocket.unity.path}")
    private String unityPath;

    @Value("${websocket.agent.path}")
    private String agentPath;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket 연결됨: {}", session.getId());

        /*
         * Unity Session
         */
        if (session.getUri() != null && session.getUri().getPath().equals(unityPath)) {
            webSocketSessionManager.addSession(unityPath, session);
            sendInitialAgentData(session, 1, new AgentPositionResponse(1, -2, 0));
            sendInitialAgentData(session, 2, new AgentPositionResponse(0, -2, 1));
        }

        /*
         * RcCar Session
         */
        if (session.getUri() != null && session.getUri().getPath().equals(agentPath)) {
            webSocketSessionManager.addSession(agentPath, session);
            sendRouteData(session, 1, List.of(1, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1));
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                try {
                    sendRouteData(session, 2, List.of(1, 1, 1, 1, 1, 2, 1, 1, 3, 1, 1));
                } catch (IOException e) {
                    log.error("Failed to send delayed route data", e);
                }
            }, 1, TimeUnit.SECONDS);
        }

        ScheduledExecutorService pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> sendPing(session), 30, 30, TimeUnit.SECONDS);

        schedulerMap.put(session.getId(), pingScheduler);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket 연결 종료됨: {}", session.getId());
        webSocketSessionManager.removeSession(unityPath, session);

        ScheduledExecutorService scheduler = schedulerMap.remove(session.getId());
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("수신된 메시지: {}", message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket 오류: {}", exception.getMessage());
        session.close();
    }

    private void sendInitialAgentData(WebSocketSession session, int agentId, AgentPositionResponse agentPosition)
            throws IOException {
        AgentDataResponse carData = new AgentDataResponse(
                agentId,
                agentPosition,
                1,
                false,
                false
        );
        String message = objectMapper.writeValueAsString(carData);
        session.sendMessage(new TextMessage(message));
        log.info("초기 agent 위치 전송 : {}", agentId);
    }

    private void sendPing(WebSocketSession session) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new PingMessage());
                log.info("특정 세션에 핑 메시지 전달 : {}", session.getId());
            }
        } catch (IOException e) {
            log.error("해당 세션에 핑 메세지 전달 실패 : {}", session.getId(), e);
        }
    }

    private void sendRouteData(WebSocketSession session, int carNumber, List<Integer> route) throws IOException {
        Map<String, Object> routeData = new HashMap<>();
        routeData.put("carNumber", carNumber);
        routeData.put("route", route);

        String message = objectMapper.writeValueAsString(routeData);
        session.sendMessage(new TextMessage(message));

        log.info("초기 route 데이터 전송 : carNumber={}, route={}", carNumber, route);
    }
}
