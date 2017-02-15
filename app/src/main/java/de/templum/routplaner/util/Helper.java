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
 * This class shares helper methods.
 * Created by simon on 08.02.2017.
 */

public class Helper {

    /**
     * Calculates the length in meters for the given route.
     * @param route for which the length should be calculated
     * @return length in meters
     */
    public static Double calculateRouteLength(List<RoutePoint> route) {
        Double length = 0.0;

        for (int i = 0; i < route.size() - 2; i++){
            length += route.get(i).getLocation().distanceTo(route.get(i + 1).getLocation());
        }

        return length;
    }

    /**
     * This method swaps two random points within the given route, but maintains the first and last entry of the list.
     * Because the first and last entry are immutable, the route needs to be at least 4.
     * @param route Route to modifiy
     */
    public static void swapRandomPoints(List<RoutePoint> route) {
        int posA, posB;

        if (route.size() < 3) return; // Fall 1 oder 2

        do {
            posA = getRandomNumberBetween(1, route.size() - 2);
            posB = getRandomNumberBetween(1, route.size() - 2);
        } while (posA == posB);

        // Swapping Point A and B
        Collections.swap(route, posA, posB);
    }

    /**
     * This method calculates the inverse distance for the provided route.
     * @param route For which the inverse should be calculated
     * @return inverse length
     */
    public static Double calculateInverseDistance(List<RoutePoint> route) {
        return -1 * Helper.calculateRouteLength(route);
    }

    /**
     * Uses the Geocoder to get an location for the provided address.
     * @param ctx Context which is needed to initialize a Geocoder
     * @param addressAsString for which an location should be found
     * @return found address or null
     */
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

    /**
     * Returns an random number which is between lower and upper bound. Includes also the bounds.
     * @param lowerBound  min value
     * @param upperBound max value
     * @return Random number which is lowerBound >= && <= upperBound
     */
    public static Integer getRandomNumberBetween(Integer lowerBound, Integer upperBound){
        Random generator = new Random();
        return generator.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    /**
     * Returns an randomly shuffled version of the given route.
     * But it still preserves the first and last entry
     * @param route to suhuffle
     * @return shuffled route
     */
    public static List<RoutePoint> randomShuffle(final List<RoutePoint> route){
        List<RoutePoint> out = route.subList(1, route.size() - 2);
        Collections.shuffle(out,new Random());
        out.add(route.get(0));
        out.add(route.get(route.size() - 1));
        return out;
    }
}
