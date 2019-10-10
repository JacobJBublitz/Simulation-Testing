package com.github.kaboomboom3.simulationtesting;

public abstract class SimulatedSystem<State, Input> {
    public abstract State simulate(State state, Input input, double dt);
}
