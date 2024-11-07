package com.ssafy.wattagatta.domain.agent.model;

import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StateKey {
    public int x;
    public int y;
    public Direction direction;
    public int timeStep;

    public static StateKey fromState(State state) {
        return new StateKey(state.x, state.y, state.direction, state.timeStep);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StateKey other)) {
            return false;
        }
        return this.x == other.x && this.y == other.y && this.direction == other.direction
                && this.timeStep == other.timeStep;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, direction, timeStep);
    }
}
