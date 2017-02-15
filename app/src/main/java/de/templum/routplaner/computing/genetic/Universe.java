package de.templum.routplaner.computing.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public Universe(List<RoutePoint> initialRoute, Integer popsize){
        mPopulation = new ArrayList<>();
        mMatingpool = new ArrayList<>();
        mPopsize = popsize;
        mEliteOffset = (int) (popsize * 0.1); // 10 Percent of the popsize

        createInitialPopulation(initialRoute);
    }

    public List<RoutePoint> evolveBetterRoute(){
        for (int i = 0; i < mEpochs; i++) {
            doEpoch();
        }
        return mPopulation.get(0).getDna();
    }

    private void doEpoch(){
        evaluation();
        selection();
        reproduction();
        mutation();
    }

    /** Phases of the genetic algorithm **/
    private void evaluation(){
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

    private void selection(){

    }

    private void reproduction(){

    }

    private void mutation(){
        for (int i = mEliteOffset; i < mPopsize; i++) {
            mPopulation.get(i).mutate();
        }
    }

    /** Helper **/
    private void createInitialPopulation(List<RoutePoint> initialRoute){
        for (int i = 0; i < mPopsize; i++) {
            mPopulation.add(new Individual(Helper.randomShuffle(initialRoute)));
        }
    }
}
