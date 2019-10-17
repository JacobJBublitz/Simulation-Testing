package com.github.kaboomboom3.simulationtesting.generic;

final class GenericState {
    private final double position;
    private final double velocity;
    private final double acceleration;

    public GenericState(double position, double velocity) {
        this(position, velocity, 0.0);
    }

    public GenericState(double position, double velocity, double acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }
}
