package com.ssafy.wattagatta.domain.agent.dto.response;

import java.util.List;

public record AgentRouteResponse(int carNumber, int goal, List<Integer> route) {
}
