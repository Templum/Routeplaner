package de.templum.routeplaner.computing.climber;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.templum.routeplaner.computing.RouteCalculator;
import de.templum.routeplaner.model.RoutePoint;
import de.templum.routeplaner.util.Helper;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;

/**
 * This class uses the hill climber algorithm to optimize the given route.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */
public class HillClimberRouteCalculator implements RouteCalculator {

    private final Integer TIMES = 100000;
    private final String TAG = HillClimberRouteCalculator.class.getCanonicalName();

    public HillClimberRouteCalculator() {}

    @Override
    public Observable<List<RoutePoint>> calculate(final List<RoutePoint> initialRoute) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RoutePoint>>>() {
            @Override
            public ObservableSource<? extends List<RoutePoint>> call() throws Exception {
                List<RoutePoint> best = initialRoute;
                Double bestFitness = Helper.calculateInverseDistance(best);
                List<RoutePoint> savedState;

                for (int i = 0; i < TIMES; i++) {
                    savedState = new ArrayList<>(best);
                    Helper.swapRandomPoints(best);

                    Double currentFitness = Helper.calculateInverseDistance(best);

                    if (currentFitness > bestFitness) {
                        // No need for restoring state, update the bestFitness
                        bestFitness = currentFitness;
                    } else {
                        // Restore the old route
                        best = savedState;
                    }
                }
                Log.i(TAG, "Finished Executing");

                return Observable.just(best);
            }
        }).subscribeOn(Schedulers.newThread());
    }
}
