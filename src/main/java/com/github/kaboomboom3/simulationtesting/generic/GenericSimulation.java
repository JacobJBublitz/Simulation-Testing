package com.github.kaboomboom3.simulationtesting.generic;

import com.github.kaboomboom3.simulationtesting.Simulation;
import com.github.kaboomboom3.simulationtesting.Units;
import com.github.sh0nk.matplotlib4j.Plot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericSimulation extends Simulation<GenericState, Double> {
    private static final GenericSystemInformation SYSTEM_INFORMATION = new GenericSystemInformation(0.899, 0.178);

    private List<Double> recordedTimes = new ArrayList<>();
    private List<GenericState> recordedStates = new ArrayList<>();
    private List<GenericState> recordedReferences = new ArrayList<>();
    private List<Double> recordedVoltages = new ArrayList<>();

    public GenericSimulation() {
        super(
                new SimulatedGenericSystem(SYSTEM_INFORMATION),
                new GenericPidController(),
                new GenericState(0.0, 0.0)
        );

        setSimulationLength(1.0 * Units.SECOND);
    }

    public static void main(String[] args) {
        new GenericSimulation().run();
    }

    @Override
    protected GenericState getReference(double time, GenericState current) {
        return new GenericState(
                10.0 * Units.FOOT,
                0.0 * Units.FOOT_PER_SECOND,
                0.0 * Units.FOOT_PER_SECOND2
        );
    }

    @Override
    protected void record(double time, GenericState current, GenericState reference, Double voltage) {
        recordedTimes.add(time);
        recordedStates.add(current);
        recordedReferences.add(reference);
        recordedVoltages.add(voltage);
    }

    @Override
    protected void makePlots(Plot plt) {
        // Position
        plt.plot()
                .add(recordedTimes, recordedStates.stream().map(state -> state.getPosition() / Units.FOOT).collect(Collectors.toList()));
        plt.plot()
                .add(recordedTimes, recordedReferences.stream().map(reference -> reference.getPosition() / Units.FOOT).collect(Collectors.toList()));
        // Velocity
//        plt.plot()
//                .add(recordedTimes, recordedStates.stream().map(state -> state.getVelocity() / Units.FOOT_PER_SECOND).collect(Collectors.toList()));
//        plt.plot()
//                .add(recordedTimes, recordedReferences.stream().map(reference -> reference.getVelocity() / Units.FOOT_PER_SECOND).collect(Collectors.toList()));
        // Acceleration
//        plt.plot()
//                .add(recordedTimes, recordedStates.stream().map(state -> state.getAcceleration() / Units.FOOT_PER_SECOND2).collect(Collectors.toList()));
//        plt.plot()
//                .add(recordedTimes, recordedReferences.stream().map(reference -> reference.getAcceleration() / Units.FOOT_PER_SECOND2).collect(Collectors.toList()));

        // Voltage
        plt.plot()
                .add(recordedTimes, recordedVoltages);
    }
}
