package com.ssafy.wattagatta.domain.agent.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteSegment {
    private int goal;
    private List<Integer> route;
    private List<Integer> abRoute;
}
