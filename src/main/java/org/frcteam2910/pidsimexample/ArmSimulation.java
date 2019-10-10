package org.frcteam2910.pidsimexample;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArmSimulation implements Runnable {
    private static final double SIMULATION_LOOP_PERIOD = 1.0 * Units.MICROSECOND;
    private static final double SIMULATION_LENGTH = 1.0 * Units.SECOND;

    private static final double RECORD_LOOP_PERIOD = 5.0 * Units.MILLISECOND;

    private static final double CONTROL_LOOP_PERIOD = 5.0 * Units.MILLISECOND;

    private static final ArmSystemInformation SYSTEM_INFORMATION = new ArmSystemInformation(
            Motors.CIM,
            (10.0 * Units.POUND) * Math.pow(1.0 * Units.FOOT, 2),
            5.0 / 1.0
    );
    private static final double ANGLE_GAIN = 1.0;
    private static final double ANGULAR_VELOCITY_GAIN = 0.12;

    private SimulatedSystem<ArmState, Double> system = new SimulatedArmSystem(SYSTEM_INFORMATION);
    private Controller<ArmState, Double> controller = new ArmController(
            ANGLE_GAIN,
            ANGULAR_VELOCITY_GAIN,
            SYSTEM_INFORMATION
    );

    @Override
    public void run() {
        int simulationIterations = (int) (SIMULATION_LENGTH / SIMULATION_LOOP_PERIOD);
        int simIterationsPerControlIteration = (int) (CONTROL_LOOP_PERIOD / SIMULATION_LOOP_PERIOD);
        int simIterationsPerRecordIteration = (int) (RECORD_LOOP_PERIOD / SIMULATION_LOOP_PERIOD);

        List<Double> recordedTimes = new ArrayList<>();
        List<Double> recordedAngles = new ArrayList<>();
        List<Double> recordedAngularVelocities = new ArrayList<>();
        List<Double> recordedReferences = new ArrayList<>();
        List<Double> recordedVoltages = new ArrayList<>();
        List<Double> recordedCurrents = new ArrayList<>();

        ArmState state = new ArmState(0.0, 0.0);
        controller.setReference(new ArmState(180.0 * Units.DEGREE, 0.0 * Units.DEGREE_PER_SECOND));

        double input = 0.0;

        for (int i = 0; i <= simulationIterations; i++) {
            if (i % simIterationsPerControlIteration == 0) {
                // Control iteration
                input = controller.calculate(state);
            }
            if (i % simIterationsPerRecordIteration == 0) {
                recordedTimes.add(i * SIMULATION_LOOP_PERIOD);
                recordedAngles.add(state.getAngle() / Units.DEGREE);
                recordedAngularVelocities.add(state.getAngularVelocity() / Units.DEGREE_PER_SECOND);
                recordedReferences.add(controller.getReference().getAngle() / Units.DEGREE);
                recordedVoltages.add(input);
                recordedCurrents.add(
                        SYSTEM_INFORMATION.getMotor().estimateCurrentDraw(
                                input,
                                SYSTEM_INFORMATION.convertSystemVelocityToMotorVelocity(state.getAngularVelocity())
                        )
                );
            }

            // Simulation iteration
            state = system.simulate(state, input, SIMULATION_LOOP_PERIOD);
        }

        Plot plt = Plot.create();
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
        try {
            plt.show();
        } catch (IOException | PythonExecutionException e) {
            e.printStackTrace();
        }
    }
}
