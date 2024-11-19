package com.ssafy.wattagatta.domain.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Constraint {
    private String agentId;
    private Node node;
    private Node fromNode;
    private Node toNode;
    private int timeStep;
    private ConstraintType type;

    public static Constraint createVertexConstraint(String agentId, Node node, int timeStep) {
        return new Constraint(agentId, node, null, null, timeStep, ConstraintType.VERTEX);
    }

    public static Constraint createEdgeConstraint(String agentId, Node fromNode, Node toNode, int timeStep) {
        return new Constraint(agentId, null, fromNode, toNode, timeStep, ConstraintType.EDGE);
    }
}
