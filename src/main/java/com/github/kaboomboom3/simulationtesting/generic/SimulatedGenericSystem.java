package com.github.kaboomboom3.simulationtesting.generic;

import com.github.kaboomboom3.simulationtesting.SimulatedSystem;

final class SimulatedGenericSystem extends SimulatedSystem<GenericState, Double> {
    private final GenericSystemInformation systemInformation;

    public SimulatedGenericSystem(GenericSystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }

    @Override
    public GenericState simulate(GenericState state, Double voltage, double dt) {
        double velocity = state.getVelocity();
        double acceleration = (-systemInformation.getVelocityConstant() * state.getVelocity() + voltage) / systemInformation.getAccelerationConstant();

        return new GenericState(
                state.getPosition() + velocity * dt,
                state.getVelocity() + acceleration * dt,
                acceleration
        );
    }
}
