package com.github.kaboomboom3.simulationtesting.generic;

final class GenericSystemInformation {
    private final double velocityConstant;
    private final double accelerationConstant;

    GenericSystemInformation(double velocityConstant, double accelerationConstant) {
        this.velocityConstant = velocityConstant;
        this.accelerationConstant = accelerationConstant;
    }

    public double getVelocityConstant() {
        return velocityConstant;
    }

    public double getAccelerationConstant() {
        return accelerationConstant;
    }
}
