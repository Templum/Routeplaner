package de.templum.routplaner.computing;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.templum.routplaner.computing.annealing.AnnealingRouteCalculator;
import de.templum.routplaner.computing.climber.HillClimberRouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by simon on 08.02.2017.
 */

public class RouteFactory {
    private final String TAG = RouteFactory.class.getSimpleName();

    private final Context mCtx;
    private List<RouteCalculator> mAlgorithms;

    public RouteFactory(final Context ctx) {
        mCtx = ctx;
        mAlgorithms = new ArrayList<>();

        // Adding our available Algorithms
        mAlgorithms.add(new AnnealingRouteCalculator());
        mAlgorithms.add(new HillClimberRouteCalculator());
        //mAlgorithms.add(new GeneticRouteCalculator());
    }

    public Observable<List<RoutePoint>> calculateRoute(final List<String> route) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RoutePoint>>>() {
            @Override
            public ObservableSource<? extends List<RoutePoint>> call() throws Exception {
                List<RoutePoint> initialRoute = new ArrayList<>();

                for (String address : route) {
                    Location location = Helper.searchBy(mCtx, address);
                    if (location != null)
                        initialRoute.add(new RoutePoint(location, location.getProvider()));
                }
                initialRoute.add(new RoutePoint(initialRoute.get(0)));
                List<RoutePoint> bestRoute = initialRoute;

                for (RouteCalculator algorithm : mAlgorithms) {
                    List<RoutePoint> calculatedRoute = algorithm.calculate(new ArrayList<>(bestRoute));

                    if (calculatedRoute != null && Helper.calculateRouteLength(bestRoute) > Helper.calculateRouteLength(calculatedRoute)) {
                        bestRoute = calculatedRoute;
                    }
                }
                return Observable.just(bestRoute);
            }
        });
    }
}
