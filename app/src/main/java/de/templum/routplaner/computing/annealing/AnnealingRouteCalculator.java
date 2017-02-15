package de.templum.routplaner.computing.annealing;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import de.templum.routplaner.computing.RouteCalculator;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;

/**
 * This class uses the simulated annealing algorithm to optimize the given route.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */
public class AnnealingRouteCalculator implements RouteCalculator {

    private final Double EPSILON = 0.01;
    private final String TAG = AnnealingRouteCalculator.class.getCanonicalName();
    private Double TEMPERATURE = 1538.0;

    //TODO: Start multiple attempts
    public AnnealingRouteCalculator() {
    }

    @Override
    public Observable<List<RoutePoint>> calculate(final List<RoutePoint> initialRoute) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RoutePoint>>>() {
            @Override
            public ObservableSource<? extends List<RoutePoint>> call() throws Exception {
                List<RoutePoint> best = initialRoute;
                Double bestFitness = Helper.calculateInverseDistance(best);
                List<RoutePoint> savedState;

                while (TEMPERATURE > EPSILON) {
                    savedState = new ArrayList<>(best);
                    Helper.swapRandomPoints(best);

                    Double currentFitness = Helper.calculateInverseDistance(best);

                    if (currentFitness > bestFitness || checkForCoolingStep(currentFitness, bestFitness)) {
                        // No need for restoring state, update the bestFitness
                        bestFitness = currentFitness;
                    } else {
                        // Restore the old route
                        best = savedState;
                    }
                    TEMPERATURE -= EPSILON;
                }
                Log.i(TAG, "Finished Executing");

                return Observable.just(best);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    private Boolean checkForCoolingStep(Double currentFitness, Double lastFitness) {
        return new Random().nextDouble() < Math.exp((currentFitness - lastFitness) / TEMPERATURE);
    }
}
