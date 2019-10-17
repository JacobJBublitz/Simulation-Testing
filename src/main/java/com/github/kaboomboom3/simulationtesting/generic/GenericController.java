package com.github.kaboomboom3.simulationtesting.generic;

import com.github.kaboomboom3.simulationtesting.Controller;

final class GenericController extends Controller<GenericState, Double> {
    private final GenericSystemInformation systemInformation;

    private final double positionGain;
    private final double velocityGain;

    GenericController(GenericSystemInformation systemInformation, double positionGain, double velocityGain) {
        this.systemInformation = systemInformation;
        this.positionGain = positionGain;
        this.velocityGain = velocityGain;
    }

    @Override
    public Double calculate(GenericState current) {
        double positionError = getReference().getPosition() - current.getPosition();
        double velocityError = getReference().getVelocity() - current.getVelocity();

        double positionEffort = positionGain * positionError;
        double velocityEffort = velocityGain * velocityError;

        return Math.max(-12.0, Math.min(
                positionEffort + velocityEffort
                        + systemInformation.getVelocityConstant() * getReference().getVelocity() * 0.8
                        + systemInformation.getAccelerationConstant() * getReference().getAcceleration() * 0.8,
                12.0));
    }
}
