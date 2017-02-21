package de.templum.routeplaner.computing.genetic;

/**
 * This class represent a couple of parents.
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

class Parents {

    private final Individual mMother;
    private final Individual mFather;

    Parents(Individual mother, Individual father) {
        mMother = mother;
        mFather = father;
    }

    Individual reproduce() {
        return mMother.orderedCrossOver(mFather);
    }
}
