package org.frcteam2910.pidsimexample;

public abstract class SimulatedSystem<State, Input> {
    public abstract State simulate(State state, Input input, double dt);
}
