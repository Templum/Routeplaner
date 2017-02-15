package de.templum.routplaner.model;

import android.location.Location;

/**
 * This class represents an list route.
 * It contains the location and the address.
 * Created by simon on 08.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */
public class RoutePoint {
    private Location mLocation;
    private String mAddress;

    public RoutePoint(Location location, String address) {
        mLocation = location;
        mAddress = address;
    }

    public RoutePoint(RoutePoint routePoint) {
        mLocation = new Location(routePoint.getLocation());
        mAddress = routePoint.toString();
    }

    public Location getLocation() {
        return mLocation;
    }

    @Override
    public String toString() {
        return mAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoutePoint) {
            return mAddress.equals(((RoutePoint) obj).mAddress);
        } else {
            return super.equals(obj);
        }
    }
}
