package com.github.kaboomboom3.simulationtesting.simple;

public final class SimpleState {
    private final double position;
    private final double velocity;

    public SimpleState(double position, double velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }
}
