package de.templum.routplaner.util;

import java.util.List;
import java.util.Random;

import de.templum.routplaner.model.RoutePoint;

/**
 * Created by simon on 08.02.2017.
 */

public class Helper {

    public static Double calculateRouteLength(List<RoutePoint> route){
        Double length = 0.0;

        RoutePoint last = null;

        for (RoutePoint current : route) {
            if(last != null){
                length += last.getLocation().distanceTo(current.getLocation());
                last = current;
            }else{
                last = current;
            }
        }
        return length;
    }
    public static void swapRandomPoints(List<RoutePoint> route){
        int posA,posB;
        Random generator = new Random();

        do{
            posA = generator.nextInt(route.size() - 2);
            posB = generator.nextInt(route.size() - 2);
        }while (posA == posB || (posA == 0 || posB == 0));

        // Swapping Point A and B
        RoutePoint temp = route.remove(posA);
        route.add(posA, route.get(posB));
        route.remove(posB);
        route.add(posB, temp);
    }
    public static Double calculateInverseDistance(List<RoutePoint> route){
        return  -1 * Helper.calculateRouteLength(route);
    }
}
