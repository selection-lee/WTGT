package com.ssafy.wattagatta.domain.agent.service;

import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.agent.utils.AStar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathCalcService {

    private final AStar aStar;

    public List<Node> calcPath(Agent agent, List<Constraint> constraints, int waitTime) {
        return aStar.findPath(agent, constraints, waitTime);
    }
}
