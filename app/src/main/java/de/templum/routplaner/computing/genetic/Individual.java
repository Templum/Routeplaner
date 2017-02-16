package de.templum.routplaner.computing.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * This class represents an Individual made of an dna and a fitness.
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

class Individual {
    private final Double MUTATION_RATE = 0.3;
    private final List<RoutePoint> mDna;
    private Double mFitness;

    Individual(List<RoutePoint> dna) {
        mDna = new ArrayList<>(dna);
    }

    void calculateFitness() {
        mFitness = Helper.calculateInverseDistance(mDna);
    }

    void mutate() {
        Random generator = new Random();
        for (int i = 0; i < mDna.size(); i++) {
            if (generator.nextDouble() < MUTATION_RATE) {
                Collections.swap(mDna, i, Helper.getRandomNumberBetween(1, mDna.size() - 2));
            }
        }
    }

    Individual orderedCrossOver(Individual otherParent) {
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

    List<RoutePoint> getDna() {
        return mDna;
    }

    Double getFitness() {
        return mFitness;
    }

}
