package de.templum.routplaner.computing;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import de.templum.routplaner.computing.annealing.AnnealingRouteCalculator;
import de.templum.routplaner.computing.climber.HillClimberRouteCalculator;
import de.templum.routplaner.computing.genetic.GeneticRouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by simon on 08.02.2017.
 */

public class RouteFactory {
    private final String TAG = RouteFactory.class.getSimpleName();

    private final Context mCtx;
    private List<RouteCalculator> mAlgorithms;

    public RouteFactory(final Context ctx){
        mCtx = ctx;
        mAlgorithms = new ArrayList<>();

        // Adding our available Algorithms
        mAlgorithms.add(new AnnealingRouteCalculator());
        mAlgorithms.add(new HillClimberRouteCalculator());
        mAlgorithms.add(new GeneticRouteCalculator());
    }

    public List<RoutePoint> calculateRoute(List<String> route){

        List<RoutePoint> initialRoute = new ArrayList<>();

        for (String address : route) {
            Location location = Helper.searchBy(mCtx, address);
            if(location != null) initialRoute.add(new RoutePoint(location, location.getProvider()));
        }

        List<RoutePoint> bestRoute = initialRoute;

        for (RouteCalculator algorithm : mAlgorithms) {
            List<RoutePoint> calculatedRoute = algorithm.calculate(new ArrayList<>(bestRoute));
            if(calculatedRoute != null && Helper.calculateRouteLength(bestRoute) < Helper.calculateRouteLength(calculatedRoute)){
                bestRoute = calculatedRoute;
            }
        }

        return bestRoute;
    }
}
