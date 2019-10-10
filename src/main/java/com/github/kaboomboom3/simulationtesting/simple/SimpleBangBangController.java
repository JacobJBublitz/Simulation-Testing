package com.github.kaboomboom3.simulationtesting.simple;

import com.github.kaboomboom3.simulationtesting.Controller;

/**
 * Implements a Bang-Bang controller
 */
public final class SimpleBangBangController extends Controller<SimpleState, Double> {
    private final double maxOutput;

    public SimpleBangBangController(double maxOutput) {
        this.maxOutput = maxOutput;
    }

    @Override
    public Double calculate(SimpleState current) {
        if (current.getPosition() > getReference().getPosition()) {
            // We are above our target so decrease our position
            return -maxOutput;
        } else if (current.getPosition() < getReference().getPosition()) {
            // We are below our target so increase our position
            return maxOutput;
        } else {
            // We are at our target so stay still
            return 0.0;
        }
    }
}
