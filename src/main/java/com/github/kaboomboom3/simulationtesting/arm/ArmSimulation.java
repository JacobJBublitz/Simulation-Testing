package com.github.kaboomboom3.simulationtesting.arm;

import com.github.kaboomboom3.simulationtesting.Motors;
import com.github.kaboomboom3.simulationtesting.Simulation;
import com.github.kaboomboom3.simulationtesting.Units;
import com.github.sh0nk.matplotlib4j.Plot;

import java.util.ArrayList;
import java.util.List;

public final class ArmSimulation extends Simulation<ArmState, Double> {
    private static final ArmSystemInformation SYSTEM_INFORMATION = new ArmSystemInformation(
            Motors.CIM,
            (10.0 * Units.POUND) * Math.pow(1.0 * Units.FOOT, 2),
            5.0 / 1.0
    );
    private static final double ANGLE_GAIN = 1.0;
    private static final double ANGULAR_VELOCITY_GAIN = 0.12;

    private List<Double> recordedTimes = new ArrayList<>();
    private List<Double> recordedAngles = new ArrayList<>();
    private List<Double> recordedAngularVelocities = new ArrayList<>();
    private List<Double> recordedReferences = new ArrayList<>();
    private List<Double> recordedVoltages = new ArrayList<>();
    private List<Double> recordedCurrents = new ArrayList<>();

    public ArmSimulation() {
        super(
                new SimulatedArmSystem(SYSTEM_INFORMATION),
                new ArmController(ANGLE_GAIN, ANGULAR_VELOCITY_GAIN, SYSTEM_INFORMATION),
                new ArmState(0.0, 0.0)
        );

        setSimulationLength(1.0 * Units.SECOND);
    }

    @Override
    protected ArmState getReference(double time, ArmState current) {
        return new ArmState(90.0 * Units.DEGREE, 0.0 * Units.DEGREE_PER_SECOND);
    }

    @Override
    protected void record(double time, ArmState current, ArmState reference, Double input) {
        recordedTimes.add(time);
        recordedAngles.add(current.getAngle() / Units.DEGREE);
        recordedAngularVelocities.add(current.getAngularVelocity() / Units.DEGREE_PER_SECOND);
        recordedReferences.add(reference.getAngle() / Units.DEGREE);
        recordedVoltages.add(input);
        recordedCurrents.add(
                SYSTEM_INFORMATION.getMotor().estimateCurrentDraw(
                        input,
                        SYSTEM_INFORMATION.convertSystemVelocityToMotorVelocity(current.getAngularVelocity())
                )
        );
    }

    @Override
    protected void makePlots(Plot plt) {
        plt.plot()
                .label("Angle")
                .add(recordedTimes, recordedAngles);
        plt.plot()
                .label("Reference")
                .add(recordedTimes, recordedReferences);
//        plt.plot()
//                .label("Angular Velocity")
//                .add(recordedTimes, recordedAngularVelocities);
        plt.plot()
                .label("Voltage")
                .add(recordedTimes, recordedVoltages);
        plt.plot()
                .label("Current")
                .add(recordedTimes, recordedCurrents);
    }
}
