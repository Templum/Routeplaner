package de.templum.routplaner.computing;

import java.util.List;

import de.templum.routplaner.model.RoutePoint;

/**
 * Created by simon on 08.02.2017.
 */
public interface RouteCalculator {
    List<RoutePoint> calculate(List<RoutePoint> initialRoute);
}
