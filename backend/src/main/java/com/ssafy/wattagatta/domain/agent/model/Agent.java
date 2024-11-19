package com.ssafy.wattagatta.domain.agent.model;

import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    private String id;
    private int startTime;
    private Node currentNode;
    private Node conveyNode;
    private Node goalNode;
    private Node homeNode;
    private AgentStatus status;
    private List<Node> currentPath;
    private Direction currentDirection;
    private boolean isMoving;
    private double batteryLevel;

    public void ready(String id, Node homeNode) {
        this.id = id;
        this.startTime = 0;
        this.homeNode = homeNode;
        this.currentNode = homeNode;
        this.conveyNode = new Node(0, 1, Direction.EAST);
        this.goalNode = null;
        this.status = AgentStatus.IDLE;
        this.currentPath = new ArrayList<>();
        this.currentDirection = Direction.EAST;
        this.isMoving = false;
        this.batteryLevel = 60.0;
    }

    public boolean isAvailable() {
        return this.status == AgentStatus.IDLE;
    }

    public void assignTask(TargetLoc targetLoc, int currentGlobalTime) {
        this.goalNode = new Node(targetLoc.x(), targetLoc.y(), Direction.NORTH);
        this.status = AgentStatus.MOVING_TO_TARGET;
        this.startTime = currentGlobalTime;
        this.currentDirection = Direction.EAST;
    }

    public void consumeBattery(int amount) {
        this.batteryLevel = Math.max(0, this.batteryLevel - amount);
    }

    public void rechargeBattery(int amount) {
        this.batteryLevel = Math.min(60, this.batteryLevel + amount);
    }
}
