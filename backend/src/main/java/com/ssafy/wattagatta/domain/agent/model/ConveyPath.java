package com.ssafy.wattagatta.domain.agent.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConveyPath {
    private List<Node> path;
    private int conveyPathSize;
}
