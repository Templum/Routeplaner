package de.templum.routplaner.computing;

import android.content.Context;

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
 * Class provides functionality to improve an given route.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
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
        //mAlgorithms.add(new GeneticRouteCalculator()); <-- Is completely functional, but its not usable on android. Blank Java Version works better
    }

    /**
     * This method tries to optimize the given route in terms of length.
     * Therefore it uses the registered algorithms.
     *
     * @param route which should be optimized
     * @return Optimized Route
     */
    public Observable<List<RoutePoint>> optimizeGivenRoute(final List<String> route) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RoutePoint>>>() {
            @Override
            public ObservableSource<? extends List<RoutePoint>> call() throws Exception {
                List<RoutePoint> bestRoute = transform(route);
                List<Observable<List<RoutePoint>>> sources = new ArrayList<>();

                sources.add(Observable.just(bestRoute));
                for (RouteCalculator algorithm : mAlgorithms) {
                    sources.add(algorithm.calculate(new ArrayList<>(bestRoute)));
                }

                return Observable.concat(sources);
            }
        });
    }

    /**
     * Takes the user provided list and transforms it into our model.
     *
     * @param list user input
     * @return user input in model form
     */
    private List<RoutePoint> transform(List<String> list) {
        return Helper.searchBy(mCtx, list);
    }
}
