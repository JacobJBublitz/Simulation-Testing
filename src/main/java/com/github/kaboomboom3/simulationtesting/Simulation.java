package com.github.kaboomboom3.simulationtesting;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;

public abstract class Simulation<State, Input> implements Runnable {
    private final Controller<State, Input> controller;
    private final SimulatedSystem<State, Input> system;
    private final State initialState;

    private double simulationLoopPeriod = 1.0 * Units.MICROSECOND;
    private double simulationLength = 5.0 * Units.SECOND;

    private double controlLoopPeriod = 5.0 * Units.MILLISECOND;

    public Simulation(SimulatedSystem<State, Input> system, Controller<State, Input> controller, State initialState) {
        this.system = system;
        this.controller = controller;
        this.initialState = initialState;
    }

    public void setSimulationLength(double simulationLength) {
        this.simulationLength = simulationLength;
    }

    public void setControlLoopPeriod(double controlLoopPeriod) {
        this.controlLoopPeriod = controlLoopPeriod;
    }

    public void setSimulationLoopPeriod(double simulationLoopPeriod) {
        this.simulationLoopPeriod = simulationLoopPeriod;
    }

    public Controller<State, Input> getController() {
        return controller;
    }

    public SimulatedSystem<State, Input> getSystem() {
        return system;
    }

    @Override
    public void run() {
        int simulationIterations = (int) (simulationLength / simulationLoopPeriod);
        int simIterationsPerControlIteration = (int) (controlLoopPeriod / simulationLoopPeriod);

        State current = initialState;
        Input input = null;

        for (int i = 0; i < simulationIterations; i++) {
            if (i % simIterationsPerControlIteration == 0) {
                double now = i * simulationLoopPeriod;

                controller.setReference(getReference(now, current));
                input = controller.calculate(current);
                record(now, current, controller.getReference(), input);
            }

            current = system.simulate(current, input, simulationLoopPeriod);
        }

        Plot plt = Plot.create();

        makePlots(plt);

        try {
            plt.show();
        } catch (IOException | PythonExecutionException e) {
            e.printStackTrace();
        }
    }

    protected abstract State getReference(double time, State current);

    protected abstract void record(double time, State current, State reference, Input input);

    protected abstract void makePlots(Plot plt);
}
