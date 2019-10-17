package com.github.kaboomboom3.simulationtesting.generic;

import com.github.kaboomboom3.simulationtesting.Controller;

public final class GenericPidController extends Controller<GenericState, Double> {
    @Override
    public Double calculate(GenericState current) {
        if (current.getPosition() > getReference().getPosition()) {
            return -12.0;
        } else {
            return 12.0;
        }
    }
}
