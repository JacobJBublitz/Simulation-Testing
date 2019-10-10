package org.frcteam2910.pidsimexample;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorSimulation implements Runnable {
    private static final double SIMULATION_LOOP_PERIOD = 1.0 * Units.MICROSECOND;
    private static final double SIMULATION_LENGTH = 2.5 * Units.SECOND;

    private static final double RECORD_LOOP_PERIOD = 5.0 * Units.MILLISECOND;

    private static final double CONTROL_LOOP_PERIOD = 5.0 * Units.MILLISECOND;

    private static final ElevatorSystemInformation SYSTEM_INFORMATION = new ElevatorSystemInformation(
            Motors.CIM,
            10.0 * Units.POUND,
            10.0 / 1.0,
            2.0 * Units.INCH
    );
    private static final double POSITION_GAIN = 1000.0;
    private static final double VELOCITY_GAIN = 0.0;

    private SimulatedSystem<ElevatorState, Double> system = new SimulatedElevatorSystem(SYSTEM_INFORMATION);
    private Controller<ElevatorState, Double> controller = new ElevatorController(
            POSITION_GAIN,
            VELOCITY_GAIN,
            SYSTEM_INFORMATION
    );

    @Override
    public void run() {
        int simulationIterations = (int) (SIMULATION_LENGTH / SIMULATION_LOOP_PERIOD);
        int simIterationsPerControlIteration = (int) (CONTROL_LOOP_PERIOD / SIMULATION_LOOP_PERIOD);
        int simIterationsPerRecordIteration = (int) (RECORD_LOOP_PERIOD / SIMULATION_LOOP_PERIOD);

        List<Double> recordedTimes = new ArrayList<>();
        List<Double> recordedPositions = new ArrayList<>();
        List<Double> recordedVelocities = new ArrayList<>();
        List<Double> recordedReferences = new ArrayList<>();
        List<Double> recordedVoltages = new ArrayList<>();
        List<Double> recordedCurrents = new ArrayList<>();

        ElevatorState state = new ElevatorState(0.0, 0.0);
        controller.setReference(new ElevatorState(10.0 * Units.FOOT, 0.0 * Units.FOOT_PER_SECOND));

        double input = 0.0;

        for (int i = 0; i <= simulationIterations; i++) {
            if (i % simIterationsPerControlIteration == 0) {
                // Control iteration
                input = controller.calculate(state);
            }
            if (i % simIterationsPerRecordIteration == 0) {
                recordedTimes.add(i * SIMULATION_LOOP_PERIOD);
                recordedPositions.add(state.getPosition() / Units.FOOT);
                recordedVelocities.add(state.getVelocity() / Units.FOOT);
                recordedReferences.add(controller.getReference().getPosition() / Units.FOOT);
                recordedVoltages.add(input);
                recordedCurrents.add(
                        SYSTEM_INFORMATION.getMotor().estimateCurrentDraw(
                                input,
                                SYSTEM_INFORMATION.convertSystemVelocityToMotorVelocity(state.getVelocity())
                        )
                );
            }

            // Simulation iteration
            state = system.simulate(state, input, SIMULATION_LOOP_PERIOD);
        }

        Plot plt = Plot.create();
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
        try {
            plt.show();
        } catch (IOException | PythonExecutionException e) {
            e.printStackTrace();
        }
    }
}
