package de.templum.routplaner.computing.genetic;

import java.util.ArrayList;
import java.util.List;

import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class Individual {
    private final List<RoutePoint> mDna;
    private Double mFitness;

    public Individual(List<RoutePoint> dna) {
        mDna = new ArrayList<>(dna);
    }

    public Individual(Individual other) {
        mDna = new ArrayList<>(other.mDna);
    }

    public void calculateFitness() {
        mFitness = Helper.calculateInverseDistance(mDna);
    }
    public void mutate() {

    }
    public Individual orderedCrossOver(Individual otherParent) {
        List<RoutePoint> childDna = new ArrayList<>(); // Important first and last position are immutable
        int start, end;

        do {
            start = Helper.getRandomNumberBetween(1, mDna.size() / 2);
            end = Helper.getRandomNumberBetween(mDna.size() / 2, mDna.size() - 2);
        } while (start == end);

        for (int i = start; i < end; i++) {
            childDna.add(new RoutePoint(mDna.get(i)));
        }

        for (int i = 1; i < otherParent.mDna.size() - 1; i++) {
            RoutePoint current = otherParent.mDna.get(i);
            if (!childDna.contains(current)) {
                childDna.add(new RoutePoint(current));
            }
        }

        childDna.add(0, new RoutePoint(mDna.get(0)));
        childDna.add(mDna.size() - 1, new RoutePoint(mDna.get(mDna.size() - 1)));
        return new Individual(childDna);
    }

    public List<RoutePoint> getDna(){
        return mDna;
    }
    public Double getFitness(){
        return mFitness;
    }

}
