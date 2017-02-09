package de.templum.routplaner.computing;

import java.util.ArrayList;
import java.util.List;

import de.templum.routplaner.computing.annealing.AnnealingRouteCalculator;
import de.templum.routplaner.computing.climber.HillClimberRouteCalculator;
import de.templum.routplaner.computing.genetic.GeneticRouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * Created by simon on 08.02.2017.
 */

public class RouteFactory {
    private final String TAG = RouteFactory.class.getSimpleName();
    private static RouteFactory mInstance = new RouteFactory();

    private List<RouteCalculator> mAlgorithms;

    private RouteFactory(){
        mAlgorithms = new ArrayList<>();

        // Adding our available Algorithms
        mAlgorithms.add(new AnnealingRouteCalculator());
        mAlgorithms.add(new HillClimberRouteCalculator());
        mAlgorithms.add(new GeneticRouteCalculator());
    }

    public static RouteFactory getInstance(){return mInstance;}

    public List<RoutePoint> calculateRoute(List<RoutePoint> route){

        List<RoutePoint> bestRoute = route;

        for (RouteCalculator algorithm : mAlgorithms) {
            List<RoutePoint> calculatedRoute = algorithm.calculate(new ArrayList<>(bestRoute));
            if(Helper.calculateRouteLength(bestRoute) < Helper.calculateRouteLength(calculatedRoute)){
                bestRoute = calculatedRoute;
            }
        }

        return bestRoute;
    }
}
