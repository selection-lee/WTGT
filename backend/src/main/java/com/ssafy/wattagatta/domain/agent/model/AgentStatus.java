package com.ssafy.wattagatta.domain.agent.model;

public enum AgentStatus {
    IDLE("대기 중"),
    MOVING_TO_TARGET("목표 위치로 이동 중"),
    PERFORMING_TASK("작업 수행 중"),
    RETURNING_HOME("복귀 중"),
    MOVING_TO_CHARGER("충전소로 이동 중"),
    PERFORMING_CHARGE("차량 충전 중");


    private final String description;

    AgentStatus(String description) {
        this.description = description;
    }
}
