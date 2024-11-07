package com.ssafy.wattagatta.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.GridInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class AgentWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket 연결됨: {}", session.getId());

        AgentResponse rcCar1 = new AgentResponse(21.49, 3.77, 11.94);
        AgentResponse rcCar2 = new AgentResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar1, rcCar2);

        String message = objectMapper.writeValueAsString(coordinates);

        session.sendMessage(new TextMessage(message));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("수신된 메시지: {}", message.getPayload());
        AgentResponse rcCar = new AgentResponse(21.49, 3.77, 11.94);
        AgentResponse rcCar2 = new AgentResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar, rcCar2);

        String response = new ObjectMapper().writeValueAsString(coordinates);
        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket 오류: {}", exception.getMessage());
        session.close();
    }

}
