package de.templum.routplaner.computing.genetic;

/**
 * This class represent a couple of parents.
 * Created by simon on 15.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class Parents {

    private final Individual mMother;
    private final Individual mFather;

    public Parents(Individual mother, Individual father) {
        mMother = mother;
        mFather = father;
    }

    public Individual reproduce() {
        return mMother.orderedCrossOver(mFather);
    }
}
