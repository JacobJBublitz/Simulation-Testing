package com.github.kaboomboom3.simulationtesting.simple;

import com.github.kaboomboom3.simulationtesting.Controller;

public final class SimpleProportionalController extends Controller<SimpleState, Double> {
    private final double gain;

    private double minOutput = Double.NEGATIVE_INFINITY;
    private double maxOutput = Double.POSITIVE_INFINITY;

    public SimpleProportionalController(double gain) {
        this.gain = gain;
    }

    public void setOutputRange(double minOutput, double maxOutput) {
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

    @Override
    public Double calculate(SimpleState current) {
        double error = getReference().getPosition() - current.getPosition();

        return Math.max(minOutput, Math.min(gain * error, maxOutput));
    }
}
