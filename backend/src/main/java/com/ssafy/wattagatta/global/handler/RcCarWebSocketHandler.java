package com.ssafy.wattagatta.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.wattagatta.domain.rccar.response.GridInfoResponse;
import com.ssafy.wattagatta.domain.rccar.response.RcCarResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class RcCarWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket 연결됨: " + session.getId());

        RcCarResponse rcCar1 = new RcCarResponse(21.49, 3.77, 11.94);
        RcCarResponse rcCar2 = new RcCarResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar1, rcCar2);

        String message = objectMapper.writeValueAsString(coordinates);

        session.sendMessage(new TextMessage(message));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("수신된 메시지: " + message.getPayload());
        RcCarResponse rcCar = new RcCarResponse(21.49, 3.77, 11.94);
        RcCarResponse rcCar2 = new RcCarResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar, rcCar2);

        String response = new ObjectMapper().writeValueAsString(coordinates);
        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket 오류: " + exception.getMessage());
        session.close();
    }

}
