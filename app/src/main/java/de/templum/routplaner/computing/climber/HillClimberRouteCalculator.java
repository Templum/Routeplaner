package de.templum.routplaner.computing.climber;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * This class uses the hill climber algorithm to optimize the given route.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */
public class HillClimberRouteCalculator implements RouteCalculator {

    private final Integer TIMES = 1000000;
    private final String TAG = HillClimberRouteCalculator.class.getCanonicalName();

    //TODO: Start multiple attempts
    public HillClimberRouteCalculator() {
    }

    @Override
    public List<RoutePoint> calculate(List<RoutePoint> initialRoute) {
        Double bestFitness = Helper.calculateInverseDistance(initialRoute);
        List<RoutePoint> savedState;

        for (int i = 0; i < TIMES; i++) {
            savedState = new ArrayList<>(initialRoute);
            Helper.swapRandomPoints(initialRoute);

            Double currentFitness = Helper.calculateInverseDistance(initialRoute);

            if (currentFitness > bestFitness) {
                // No need for restoring state, update the bestFitness
                Log.d(TAG, "Found a better route");
                bestFitness = currentFitness;
            } else {
                // Restore the old route
                Log.d(TAG, "Found a worse route");
                initialRoute = savedState;
            }
        }
        return initialRoute;
    }
}
