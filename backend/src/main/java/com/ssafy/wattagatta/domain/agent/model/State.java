package com.ssafy.wattagatta.domain.agent.model;

import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class State implements Comparable<State> {
    public int x;
    public int y;
    public Direction direction;
    public int timeStep;
    public State prevState;
    public double g;
    public double h;
    public double f;
    public Node node;
    

    public static State createStartState(Agent agent, double heuristic) {
        double g = 0;
        double f = g + heuristic;
        return new State(
                agent.getCurrentNode().getX(),
                agent.getCurrentNode().getY(),
                agent.getCurrentNode().getDirection(),
                agent.getStartTime(),
                null,
                g,
                heuristic,
                f,
                agent.getCurrentNode()
        );
    }

    public static State createUpdatedState(State existingState, State prevState, double updatedG) {
        double f = updatedG + existingState.h;
        return new State(
                existingState.x,
                existingState.y,
                existingState.direction,
                existingState.timeStep,
                prevState,
                updatedG,
                existingState.h,
                f,
                existingState.node
        );
    }


    public static State getNextState(State currentState,
                                     int nextTimeStep, double gCost, double h, Node nextNode) {
        double f = gCost + h;
        return new State(nextNode.getX(), nextNode.getY(), nextNode.getDirection(), nextTimeStep, currentState, gCost,
                h, f, nextNode);
    }


    @Override
    public int compareTo(State other) {
        return Double.compare(this.f, other.f);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State other)) {
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
