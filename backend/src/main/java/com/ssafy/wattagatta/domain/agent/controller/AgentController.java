package com.ssafy.wattagatta.domain.agent.controller;

import com.ssafy.wattagatta.domain.agent.dto.response.AgentResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.GridInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgentController {

    @GetMapping("/api/rccar/test")
    public GridInfoResponse testUnity() {
        AgentResponse rcCar = new AgentResponse(21.49, 3.77, 11.94);
        AgentResponse rcCar2 = new AgentResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar, rcCar2);
        return coordinates;
    }
}
