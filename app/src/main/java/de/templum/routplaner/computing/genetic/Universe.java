package de.templum.routplaner.computing.genetic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class Universe {

    private final String TAG = Universe.class.getCanonicalName();
    private List<Individual> mPopulation;
    private List<Individual> mMatingpool;
    private Integer mEliteOffset;
    private Integer mPopsize;
    private Integer mEpochs = 1000000;

    public Universe(Integer popsize) {
        mPopulation = new ArrayList<>();
        mMatingpool = new ArrayList<>();
        mPopsize = popsize;
        mEliteOffset = (int) (popsize * 0.1); // 10 Percent of the popsize
    }

    public List<RoutePoint> evolveBetterRoute(List<RoutePoint> initialRoute) {
        createInitialPopulation(initialRoute);

        for (int i = 0; i < mEpochs; i++) {
            doEpoch();
        }

        return mPopulation.get(0).getDna();
    }

    private void doEpoch() {
        evaluation();
        reproduction();
        mutation();

        Log.i(TAG, "Best Route has a distance of " + mPopulation.get(0).getFitness());
    }

    /**
     * Phases of the genetic algorithm
     **/
    private void evaluation() {
        for (Individual i : mPopulation) {
            i.calculateFitness();
        }
        Collections.sort(mPopulation, new Comparator<Individual>() {
            @Override
            public int compare(Individual individual, Individual other) {
                return individual.getFitness().compareTo(other.getFitness());
            }
        });
    }

    private void reproduction() {
        mMatingpool.addAll(mPopulation.subList(0, mPopsize / 2));

        Double totalFitness = 0.0;
        for (int i = 1; i < mPopulation.size(); i++) {
            totalFitness += mPopulation.get(i).getFitness();
        }

        for (int i = mPopsize / 2; i < mPopsize; i++) {
            mMatingpool.add(rouletteSelection(totalFitness).reproduce());
        }
    }

    private void mutation() {
        for (int i = mEliteOffset; i < mPopsize; i++) {
            mPopulation.get(i).mutate();
        }
    }

    /**
     * Helper
     **/
    private void createInitialPopulation(List<RoutePoint> initialRoute) {
        for (int i = 0; i < mPopsize; i++) {
            mPopulation.add(new Individual(Helper.randomShuffle(initialRoute)));
        }
    }

    private Parents rouletteSelection(Double totalFitness) {
        return new Parents(spinRouletteWheel(totalFitness), spinRouletteWheel(totalFitness));
    }

    private Individual spinRouletteWheel(Double totalFitness) {
        Double randNo = new Random().nextDouble();
        Double sum = 0.0;
        Integer index = Helper.getRandomNumberBetween(0, mPopsize - 1);

        while (sum < randNo) {
            index = (++index) % mPopsize;
            if (totalFitness > 0)
                sum += mPopulation.get(index).getFitness() / totalFitness;
            else
                sum += 1D / mPopsize;
        }
        return mPopulation.get(index);
    }
}
