package de.templum.routplaner.computing.annealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * This class uses the simulated annealing algorithm to optimize the given route.
 * Created by simon on 08.02.2017.
 */
public class AnnealingRouteCalculator implements RouteCalculator {

    private final Double EPSILON = 0.001;
    private Double TEMPERATURE = 1538.0; //TODO: Search a better Temperature
    private final String TAG = AnnealingRouteCalculator.class.getCanonicalName();

    //TODO: Start multiple attempts
    public AnnealingRouteCalculator() {
    }

    @Override
    public List<RoutePoint> calculate(List<RoutePoint> initialRoute) {
        Double bestFitness = Helper.calculateInverseDistance(initialRoute);
        List<RoutePoint> savedState;

        while (TEMPERATURE > EPSILON) {
            savedState = new ArrayList<>(initialRoute);
            Helper.swapRandomPoints(initialRoute);

            Double currentFitness = Helper.calculateInverseDistance(initialRoute);

            if (currentFitness > bestFitness || checkForCoolingStep(currentFitness, bestFitness)) {
                // No need for restoring state, update the bestFitness
                bestFitness = currentFitness;
            } else {
                // Restore the old route
                initialRoute = savedState;
            }
            TEMPERATURE -= EPSILON;
        }
        return initialRoute;
    }

    private Boolean checkForCoolingStep(Double currentFitness, Double lastFitness) {
        return new Random().nextDouble() < Math.exp((currentFitness - lastFitness) / TEMPERATURE);
    }
}
