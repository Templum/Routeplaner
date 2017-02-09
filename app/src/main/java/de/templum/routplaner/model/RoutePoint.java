package de.templum.routplaner.model;

import android.location.Location;

/**
 * Created by simon on 08.02.2017.
 */
public class RoutePoint {
    private Location mLocation;
    private Boolean mIsStart;

    public RoutePoint(Location location, Boolean isStart) {
        mLocation = location;
        mIsStart = isStart;
    }

    public Location getLocation() {
        return mLocation;
    }

    public Boolean getIsStart() {
        return mIsStart;
    }

    @Override
    public String toString() {
        return "RoutePoint: " + mLocation.getProvider(); //TODO: Maybe Wrong Update if needed
    }
}
