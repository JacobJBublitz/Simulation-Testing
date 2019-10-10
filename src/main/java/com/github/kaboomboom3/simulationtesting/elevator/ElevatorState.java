package com.github.kaboomboom3.simulationtesting.elevator;

public final class ElevatorState {
    private final double position;
    private final double velocity;

    public ElevatorState(double position, double velocity) {
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
