package de.templum.routplaner.model;

import android.location.Location;

/**
 * Created by simon on 08.02.2017.
 */
public class RoutePoint {
    private Location mLocation;
    private String mAddress;

    public RoutePoint(Location location, String address) {
        mLocation = location;
        mAddress = address;
    }

    public RoutePoint(RoutePoint routePoint) {
        mLocation = routePoint.getLocation();
        mAddress = routePoint.toString();
    }

    public Location getLocation() {
        return mLocation;
    }

    @Override
    public String toString() {
        return mAddress;
    }
}
