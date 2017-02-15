package de.templum.routplaner.computing;

import java.util.List;

import de.templum.routplaner.model.RoutePoint;

/**
 * Interface which needs to implemented by each computing algorithm.
 * Created by simon on 08.02.2017.
 */
public interface RouteCalculator {
    List<RoutePoint> calculate(List<RoutePoint> initialRoute);
}
