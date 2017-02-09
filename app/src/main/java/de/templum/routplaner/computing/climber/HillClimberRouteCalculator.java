package de.templum.routplaner.computing.climber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * Created by simon on 08.02.2017.
 */

public class HillClimberRouteCalculator implements RouteCalculator {

    private final Integer TIMES = 1000000;

    //TODO: Start multiple attempts
    public HillClimberRouteCalculator(){}

    @Override
    public List<RoutePoint> calculate(List<RoutePoint> initialRoute) {
        Double bestFitness = Helper.calculateInverseDistance(initialRoute);
        List<RoutePoint> savedState;

        for (int i = 0; i < TIMES; i++){
            savedState = new ArrayList<>(initialRoute);
            Helper.swapRandomPoints(initialRoute);

            Double currentFitness = Helper.calculateInverseDistance(initialRoute);

            if( currentFitness > bestFitness){
                // No need for restoring state, update the bestFitness
                bestFitness = currentFitness;
            }else{
                // Restore the old route
                initialRoute = savedState;
            }
        }
        return initialRoute;
    }
}
