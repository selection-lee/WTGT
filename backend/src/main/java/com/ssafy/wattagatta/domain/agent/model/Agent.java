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
    private Node goalNode;
    private Node homeNode;
    private AgentStatus status;
    private List<Node> currentPath;
    private Direction currentDirection;
    private boolean isMoving;

    public void ready(String id, Node homeNode) {
        this.id = id;
        this.startTime = 0;
        this.homeNode = homeNode;
        this.currentNode = homeNode;
        this.goalNode = null;
        this.status = AgentStatus.IDLE;
        this.currentPath = new ArrayList<>();
        this.currentDirection = Direction.EAST;
        this.isMoving = false;
    }

    public boolean isAvailable() {
        return this.status == AgentStatus.IDLE;
    }

    public void assignTask(TargetLoc targetLoc, int currentGlobalTime) {
        this.goalNode = new Node(targetLoc.x(), targetLoc.y(), Direction.NORTH);
        this.status = AgentStatus.MOVING_TO_TARGET;
        this.startTime = currentGlobalTime;
    }

    public void assignReturnHomeTask(int currentGlobalTime) {
        this.goalNode = this.homeNode;
        this.status = AgentStatus.RETURNING_HOME;
        this.startTime = currentGlobalTime;
    }

    public void moveAlongPath(Consumer<Node> positionUpdateCallback, Runnable onPathComplete, Runnable onTaskComplete,
                              Consumer<Node> returnPositionUpdateCallback, Runnable onReturnComplete) {
        if (isMoving || currentPath == null || currentPath.isEmpty()) {
            return;
        }
        isMoving = true;

        new Thread(() -> {
            try {
                while (!currentPath.isEmpty()) {
                    Node nextNode = currentPath.remove(0);
                    setCurrentNode(nextNode);
                    positionUpdateCallback.accept(nextNode);
                    Thread.sleep(1000);
                }
                onPathComplete.run();
                onTaskComplete.run();
                while (!currentPath.isEmpty()) {
                    Node nextNode = currentPath.remove(0);
                    setCurrentNode(nextNode);
                    returnPositionUpdateCallback.accept(nextNode);
                    Thread.sleep(1000);
                }
                onReturnComplete.run();
            } catch (InterruptedException e) {
                log.error("이동 중 예외 발생", e);
                Thread.currentThread().interrupt();
            } finally {
                isMoving = false;
            }
        }).start();
    }

    public void performTask(int taskDurationTime) {
        if (status != AgentStatus.MOVING_TO_TARGET) {
            log.warn("에이전트가 목표 위치로 이동하지 않았습니다 : {}", status);
            return;
        }
        log.info("에이전트 {}가 현재 위치에서 작업을 수행합니다.", id);
        status = AgentStatus.PERFORMING_TASK;
        try {
            Thread.sleep(taskDurationTime);
            log.info("에이전트 {} 작업 완료", id);
        } catch (InterruptedException e) {
            log.error("작업 수행 중 예외 발생", e);
            Thread.currentThread().interrupt();
        }
    }
}
