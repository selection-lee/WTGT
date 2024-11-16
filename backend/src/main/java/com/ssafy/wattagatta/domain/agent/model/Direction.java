package com.ssafy.wattagatta.domain.agent.model;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction turnLeft() {
        return switch (this) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

    public int[] moveForward(int x, int y) {
        return switch (this) {
            case NORTH -> new int[]{x + 1, y};
            case EAST -> new int[]{x, y + 1};
            case SOUTH -> new int[]{x - 1, y};
            case WEST -> new int[]{x, y - 1};
        };
    }
}
