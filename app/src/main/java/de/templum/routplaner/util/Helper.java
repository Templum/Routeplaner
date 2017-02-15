package de.templum.routplaner.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.model.RoutePoint;

/**
 * Created by simon on 08.02.2017.
 */

public class Helper {

    public static Double calculateRouteLength(List<RoutePoint> route) {
        Double length = 0.0;

        for (int i = 0; i < route.size() - 2; i++){
            length += route.get(i).getLocation().distanceTo(route.get(i + 1).getLocation());
        }

        return length;
    }

    public static void swapRandomPoints(List<RoutePoint> route) {
        int posA, posB;
        Random generator = new Random();

        if (route.size() < 3) return; // Fall 1 oder 2

        do {
            posA = generator.nextInt(route.size() - 2);
            posB = generator.nextInt(route.size() - 2);
        } while (posA == posB || (posA == 0 || posB == 0));

        // Swapping Point A and B
        Collections.swap(route, posA, posB);
    }

    public static Double calculateInverseDistance(List<RoutePoint> route) {
        return -1 * Helper.calculateRouteLength(route);
    }

    public static Location searchBy(Context ctx, String addressAsString) {
        Geocoder geocoder = new Geocoder(ctx);

        try {
            List<Address> findings = geocoder.getFromLocationName(addressAsString, 10);

            if (findings.size() >= 1) {
                Address address = findings.get(0);
                Location location = new Location(addressAsString);
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());
                return location;
            } else {
                return null;
            }

        } catch (IOException e) {
            return null;
        }
    }
}
