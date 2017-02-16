package de.templum.routplaner.computing.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;

/**
 * This class represents the universe in which the solution is being evolved.
 * It holds an population with total size of popsize. And it runs for 500 epochs.
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

class Universe {

    private final String TAG = Universe.class.getCanonicalName();
    private final Integer mEpochs = 500; // Adjust to fit needs
    private List<Individual> mPopulation;
    private List<Individual> mMatingpool;
    private Integer mEliteOffset;
    private Integer mPopsize;

    Universe(Integer popsize) {
        mPopulation = new ArrayList<>();
        mMatingpool = new ArrayList<>();
        mPopsize = popsize;
        mEliteOffset = (int) (popsize * 0.1); // 10 Percent of the popsize
    }

    /**
     * Takes in the route an creates an new population with it.
     * Afterwards an solution is being evolved for mEpochs.
     *
     * @param initialRoute route which should be optimized
     * @return optimized route
     */
    List<RoutePoint> evolveBetterRoute(List<RoutePoint> initialRoute) {
        createInitialPopulation(initialRoute);

        for (int i = 0; i < mEpochs; i++) {
            doEpoch();
        }

        return mPopulation.get(0).getDna();
    }

    /**
     * Does the evolutionary cycle for one generation
     */
    private void doEpoch() {
        evaluation();
        reproduction();
        mutation();
    }

    /**
     * Phases of the genetic algorithm
     **/

    /**
     * Evaluates the individuals using the fitness function.
     * Afterwards the population gets sorted.
     */
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

    /**
     * Only the (best) half move on to the next generation. The rest gets destroyed.
     * Uses the roulette selection algorithm to select parents for reproduction of
     * the missing half.
     */
    private void reproduction() {
        mMatingpool.addAll(mPopulation.subList(0, mPopsize / 2));

        Double totalFitness = 0.0;
        for (int i = 1; i < mPopulation.size(); i++) {
            totalFitness += mPopulation.get(i).getFitness();
        }

        for (int i = mPopsize / 2; i < mPopsize; i++) {
            mMatingpool.add(rouletteSelection(totalFitness).reproduce());
        }
        mPopulation.clear();
        mPopulation.addAll(mMatingpool);
        mMatingpool.clear();
    }

    /**
     * Besides the elite of an generation (best 10%) every individual mutates.
     * Saving the best 10% allows us to have an stable solution.
     */
    private void mutation() {
        for (int i = mEliteOffset; i < mPopsize; i++) {
            mPopulation.get(i).mutate();
        }
    }

    /**
     * Uses the given route to (randomly) generate the initial population.
     *
     * @param initialRoute route which should be optimized
     */
    private void createInitialPopulation(List<RoutePoint> initialRoute) {
        for (int i = 0; i < mPopsize; i++) {
            mPopulation.add(new Individual(Helper.randomShuffle(new ArrayList<>(initialRoute))));
        }
    }

    /**
     * Searches parents using the roulette selection algorithm.
     *
     * @param totalFitness total fitness of the generation
     * @return randomly selected parents
     */
    private Parents rouletteSelection(Double totalFitness) {
        return new Parents(spinRouletteWheel(totalFitness), spinRouletteWheel(totalFitness));
    }

    /**
     * Selects an individual in respect to the totalFitness based on its on fitness.
     * The higher the fitness, the higher is the chance to be selected.
     *
     * @param totalFitness total fitness of the generation
     * @return randomly selected individual
     */
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
