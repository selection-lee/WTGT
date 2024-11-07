package com.ssafy.wattagatta.domain.agent.model;

public enum AgentAction {
    WAIT("대기", 0),
    MOVE_FORWARD("전진", 1),
    TURN_LEFT("좌회전", 2),
    TURN_RIGHT("우회전", 3);


    private String description;
    private int direction;

    AgentAction(String description, int direction) {
        this.description = description;
        this.direction = direction;
    }
}
