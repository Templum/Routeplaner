package de.templum.routplaner.computing.genetic;

import java.util.List;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;

/**
 * This class uses an genetic algorithm to find the optimal route
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class GeneticRouteCalculator implements RouteCalculator {

    private final Universe mUniverse;

    public GeneticRouteCalculator() {
        mUniverse = new Universe(1000); //TODO: Maybe Adjust
    }

    @Override
    public List<RoutePoint> calculate(List<RoutePoint> initialRoute) {
        return mUniverse.evolveBetterRoute(initialRoute);
    }
}
