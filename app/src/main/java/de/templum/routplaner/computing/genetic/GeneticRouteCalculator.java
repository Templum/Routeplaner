package de.templum.routplaner.computing.genetic;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;

/**
 * This class uses an genetic algorithm to find the optimal route
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class GeneticRouteCalculator implements RouteCalculator {

    private final Universe mUniverse;
    private final String TAG = GeneticRouteCalculator.class.getCanonicalName();

    public GeneticRouteCalculator() {
        mUniverse = new Universe(500);
    }

    @Override
    public Observable<List<RoutePoint>> calculate(final List<RoutePoint> initialRoute) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RoutePoint>>>() {
            @Override
            public ObservableSource<? extends List<RoutePoint>> call() throws Exception {
                List<RoutePoint> result = mUniverse.evolveBetterRoute(initialRoute);
                Log.i(TAG, "Finished Executing");
                return Observable.just(result);

            }
        }).subscribeOn(Schedulers.newThread());
    }
}
