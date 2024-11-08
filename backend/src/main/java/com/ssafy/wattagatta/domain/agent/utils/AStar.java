package com.ssafy.wattagatta.domain.agent.utils;


import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.AgentAction;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.ConstraintType;
import com.ssafy.wattagatta.domain.agent.model.Direction;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.agent.model.State;
import com.ssafy.wattagatta.domain.agent.model.StateKey;
import com.ssafy.wattagatta.global.utils.GlobalClock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AStar {

    private final GlobalClock globalClock;

    public List<Node> findPath(Agent agent, List<Constraint> constraints) {
        PriorityQueue<State> openList = new PriorityQueue<>();
        Map<StateKey, State> closedList = new HashMap<>();
        Node goalNode = agent.getGoalNode();

        State startState = State.createStartState(agent, heuristic(agent.getCurrentNode(), goalNode));

        openList.add(startState);

        while (!openList.isEmpty()) {
            State currentState = openList.poll();
            StateKey currentKey = StateKey.fromState(currentState);

            if (isGoalState(currentState, agent.getGoalNode())) {
                return reconstructPath(currentState, 5);
            }

            closedList.put(currentKey, currentState);

            for (AgentAction action : AgentAction.values()) {
                State nextState = getNextState(currentState, action, goalNode);
                if (nextState == null) {
                    continue;
                }

                if (isConstrained(agent.getId(), currentState.node, nextState.node, nextState.timeStep, constraints)) {
                    continue;
                }

                StateKey nextKey = StateKey.fromState(nextState);
                if (closedList.containsKey(nextKey)) {
                    continue;
                }

                double tentativeG = nextState.g;

                Optional<State> existingStateOpt = openList.stream()
                        .filter(s -> s.equals(nextState))
                        .findFirst();

                if (existingStateOpt.isPresent()) {
                    State existingState = existingStateOpt.get();
                    if (tentativeG < existingState.g) {
                        openList.remove(existingState);

                        State updatedState = State.createUpdatedState(existingState, currentState, tentativeG);

                        openList.add(updatedState);
                    }
                } else {
                    nextState.g = tentativeG;
                    openList.add(nextState);
                }
            }
        }
        return null;
    }

    public boolean isGoalState(State state, Node goalNode) {
        return state.x == goalNode.getX() && state.y == goalNode.getY();
    }

    public double heuristic(Node currentNode, Node goalNode) {
        int dx = Math.abs(currentNode.getX() - goalNode.getX());
        int dy = Math.abs(currentNode.getY() - goalNode.getY());
        int distance = dx + dy;

        int directionCost = (currentNode.getDirection() != goalNode.getDirection()) ? 1 : 0;

        return distance + directionCost;
    }

    public List<Node> reconstructPath(State state, int waitTime) {
        List<Node> path = new ArrayList<>();
        State current = state;
        while (current != null) {
            path.add(0, current.node);
            current = current.prevState;
        }
        Node goalNode = path.get(path.size() - 1);
        for (int i = 0; i < waitTime; i++) {
            path.add(goalNode);
        }
        return path;
    }

    public State getNextState(State currentState, AgentAction action, Node goalNode) {
        int nextX = currentState.x;
        int nextY = currentState.y;
        Direction nextDirection = currentState.direction;
        int nextTimeStep = currentState.timeStep + 1;
        double gCost = currentState.g;

        switch (action) {
            case MOVE_FORWARD:
                int[] nextCoords = currentState.direction.moveForward(nextX, nextY);
                nextX = nextCoords[0];
                nextY = nextCoords[1];
                gCost += 1;
                break;
            case TURN_LEFT:
                nextDirection = currentState.direction.turnLeft();
                gCost += 1;
                break;
            case TURN_RIGHT:
                nextDirection = currentState.direction.turnRight();
                gCost += 1;
                break;
            case WAIT:
                gCost += 0.5;
                break;
            default:
                return null;
        }

        if (!isValidPosition(nextX, nextY)) {
            return null;
        }

        Node nextNode = new Node(nextX, nextY, nextDirection);

        double h = heuristic(nextNode, goalNode);
        return State.getNextState(currentState, nextTimeStep, gCost, h, nextNode);
    }

    public boolean isValidPosition(int x, int y) {
        int gridWidth = 9;
        int gridHeight = 9;
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    public boolean isConstrained(String agentId, Node fromNode, Node toNode, int timeStep,
                                 List<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            if (!constraint.getAgentId().equals(agentId)) {
                continue;
            }

            if (constraint.getTimeStep() != timeStep) {
                continue;
            }

            if (constraint.getType() == ConstraintType.VERTEX) {
                if (constraint.getNode().equals(toNode)) {
                    return true;
                }
            } else if (constraint.getType() == ConstraintType.EDGE) {
                if (constraint.getFromNode().equals(fromNode) && constraint.getToNode().equals(toNode)) {
                    return true;
                }
            }
        }
        return false;
    }

}
