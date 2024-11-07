package com.ssafy.wattagatta.domain.agent.model;

public enum ConstraintType {
    VERTEX("정점 충돌"),
    EDGE("간선 충돌");

    private String description;

    ConstraintType(String description) {
        this.description = description;
    }
}
