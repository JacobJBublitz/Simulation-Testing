package com.github.kaboomboom3.simulationtesting.elevator;

import com.github.kaboomboom3.simulationtesting.Motors;
import com.github.kaboomboom3.simulationtesting.Simulation;
import com.github.kaboomboom3.simulationtesting.Units;
import com.github.sh0nk.matplotlib4j.Plot;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSimulation extends Simulation<ElevatorState, Double> {
    private static final ElevatorSystemInformation SYSTEM_INFORMATION = new ElevatorSystemInformation(
            Motors.CIM,
            10.0 * Units.POUND,
            10.0 / 1.0,
            2.0 * Units.INCH
    );
    private static final double POSITION_GAIN = 1000.0;
    private static final double VELOCITY_GAIN = 0.0;

    private List<Double> recordedTimes = new ArrayList<>();
    private List<Double> recordedPositions = new ArrayList<>();
    private List<Double> recordedVelocities = new ArrayList<>();
    private List<Double> recordedReferences = new ArrayList<>();
    private List<Double> recordedVoltages = new ArrayList<>();
    private List<Double> recordedCurrents = new ArrayList<>();

    public ElevatorSimulation() {
        super(
                new SimulatedElevatorSystem(SYSTEM_INFORMATION),
                new ElevatorController(POSITION_GAIN, VELOCITY_GAIN, SYSTEM_INFORMATION),
                new ElevatorState(0.0, 0.0)
        );

        setSimulationLength(2.0 * Units.SECOND);
    }

    @Override
    protected ElevatorState getReference(double time, ElevatorState current) {
        return new ElevatorState(10.0 * Units.FOOT, 0.0 * Units.FOOT_PER_SECOND);
    }

    @Override
    protected void record(double time, ElevatorState current, ElevatorState reference, Double input) {
        recordedTimes.add(time);
        recordedPositions.add(current.getPosition() / Units.FOOT);
        recordedVelocities.add(current.getVelocity() / Units.FOOT);
        recordedReferences.add(reference.getPosition() / Units.FOOT);
        recordedVoltages.add(input);
        recordedCurrents.add(
                SYSTEM_INFORMATION.getMotor().estimateCurrentDraw(
                        input,
                        SYSTEM_INFORMATION.convertSystemVelocityToMotorVelocity(current.getVelocity())
                )
        );
    }

    @Override
    protected void makePlots(Plot plt) {
        plt.plot()
                .label("Position")
                .add(recordedTimes, recordedPositions);
        plt.plot()
                .label("Reference")
                .add(recordedTimes, recordedReferences);
        plt.plot()
                .label("Velocity")
                .add(recordedTimes, recordedVelocities);
        plt.plot()
                .label("Voltage")
                .add(recordedTimes, recordedVoltages);
        plt.plot()
                .label("Current")
                .add(recordedTimes, recordedCurrents);
    }
}
