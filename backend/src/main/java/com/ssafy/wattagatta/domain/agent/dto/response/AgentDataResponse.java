package com.ssafy.wattagatta.domain.agent.dto.response;

public record AgentDataResponse(int rc_car_id, AgentPositionResponse rc_car, int rc_car_angle, boolean arrive,
                                boolean convey_arrive) {
}
