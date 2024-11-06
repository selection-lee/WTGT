package com.ssafy.wattagatta.domain.agent.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    private String id;
    private int startTime;
    private Node currentNode;
    private Node goalNode;
    private Node homeNode;
    private AgentStatus status;
    private List<Node> currentPath;
    private Direction currentDirection;

    public void ready(String id, Node homeNode) {
        this.id = id;
        this.startTime = 0;
        this.homeNode = homeNode;
        this.currentNode = homeNode;
        this.goalNode = null;
        this.status = AgentStatus.IDLE;
        this.currentPath = new ArrayList<>();
        this.currentDirection = Direction.EAST;
    }

    public boolean isAvailable() {
        return this.status == AgentStatus.IDLE;
    }
}
