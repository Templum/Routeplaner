package de.templum.routeplaner.computing;

import java.util.List;

import de.templum.routeplaner.model.RoutePoint;
import io.reactivex.Observable;

/**
 * Interface which needs to implemented by each computing algorithm.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */
public interface RouteCalculator {
    Observable<List<RoutePoint>> calculate(List<RoutePoint> initialRoute);
}
