package com.ssafy.wattagatta.domain.agent.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {
    private int x;
    private int y;
    private Direction direction;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node other)) {
            return false;
        }
        return this.x == other.x && this.y == other.y && this.direction == other.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, direction);
    }

    public boolean equalsPosition(Node other) {
        if (other == null) {
            return false;
        }

        return this.x == other.x && this.y == other.y;
    }
}
