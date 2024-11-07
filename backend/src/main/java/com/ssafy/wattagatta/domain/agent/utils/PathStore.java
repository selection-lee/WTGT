package com.ssafy.wattagatta.domain.agent.utils;

import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathStore {

    private final Map<String, Map<Integer, Node>> agentPaths = Collections.synchronizedMap(new HashMap<>());

    public void setPosition(String agentId, int timeStep, Node position) {
        agentPaths.computeIfAbsent(agentId, k -> Collections.synchronizedMap(new HashMap<>())).put(timeStep, position);
    }

    public Node getPosition(String agentId, int timeStep) {
        if (!agentPaths.containsKey(agentId)) {
            return null;
        }
        return agentPaths.get(agentId).get(timeStep);
    }

    public void savePath(String agentId, int startTime, List<Node> path) {
        for (int t = 0; t < path.size(); t++) {
            setPosition(agentId, startTime + t, path.get(t));
        }
    }

    public void reserveTaskLocation(String agentId, int startTime, Node lastNode, int taskDuration) {
        for (int t = 0; t < taskDuration; t++) {
            setPosition(agentId, startTime + t, lastNode);
        }
    }

    public Map<String, Map<Integer, Node>> getAllPositions() {
        return new HashMap<>(agentPaths);
    }

    public List<Constraint> getConstraintsForAgent(String currentAgentId) {
        List<Constraint> constraints = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, Node>> entry : agentPaths.entrySet()) {
            String agentId = entry.getKey();
            if (agentId.equals(currentAgentId)) {
                continue;
            }

            Map<Integer, Node> agentPositionMap = entry.getValue();
            for (Map.Entry<Integer, Node> posEntry : agentPositionMap.entrySet()) {
                int timeStep = posEntry.getKey();
                Node node = posEntry.getValue();
                constraints.add(Constraint.createVertexConstraint(currentAgentId, node, timeStep));
            }

            List<Integer> sortedTimeSteps = new ArrayList<>(agentPositionMap.keySet());
            Collections.sort(sortedTimeSteps);
            for (int i = 1; i < sortedTimeSteps.size(); i++) {
                int currentTime = sortedTimeSteps.get(i);
                int previousTime = sortedTimeSteps.get(i - 1);
                Node fromNode = agentPositionMap.get(previousTime);
                Node toNode = agentPositionMap.get(currentTime);
                constraints.add(Constraint.createEdgeConstraint(currentAgentId, fromNode, toNode, currentTime));
            }
        }
        return constraints;
    }

    public void removePath(String agentId) {
        agentPaths.remove(agentId);
    }
}
