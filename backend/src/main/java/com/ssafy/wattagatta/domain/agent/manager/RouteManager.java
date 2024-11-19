package com.ssafy.wattagatta.domain.agent.manager;

import com.ssafy.wattagatta.domain.agent.model.RouteSegment;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class RouteManager {

    private Map<Integer, List<RouteSegment>> carRoutesMap = new ConcurrentHashMap<>();
    private Map<Integer, Integer> carCurrentGoalMap = new ConcurrentHashMap<>();

    public void initializeRoutes(int carNumber, List<RouteSegment> routes) {
        carRoutesMap.put(carNumber, routes);
        carCurrentGoalMap.put(carNumber, 0);
    }

    public RouteSegment getNextRoute(int carNumber) {
        int currentGoalIndex = carCurrentGoalMap.getOrDefault(carNumber, 0);
        List<RouteSegment> routes = carRoutesMap.get(carNumber);

        if (routes != null && currentGoalIndex < routes.size()) {
            RouteSegment nextRoute = routes.get(currentGoalIndex);
            carCurrentGoalMap.put(carNumber, currentGoalIndex + 1);
            return nextRoute;
        } else {
            return null;
        }
    }

}
