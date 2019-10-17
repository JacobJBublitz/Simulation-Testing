package com.github.kaboomboom3.simulationtesting.simple;

import com.github.kaboomboom3.simulationtesting.*;
import com.github.sh0nk.matplotlib4j.Plot;

import java.util.ArrayList;
import java.util.List;

public final class SimpleSimulation extends Simulation<SimpleState, Double> {
    private List<Double> recordedTimes = new ArrayList<>();
    private List<Double> recordedPositions = new ArrayList<>();
    private List<Double> recordedVelocities = new ArrayList<>();
    private List<Double> recordedReferences = new ArrayList<>();
    private List<Double> recordedVoltages = new ArrayList<>();


    public SimpleSimulation(SimulatedSystem<SimpleState, Double> system, Controller<SimpleState, Double> controller) {
        super(system, controller, new SimpleState(0.0 * Units.FOOT, 0.0 * Units.FOOT_PER_SECOND));
    }

    public static SimpleSimulation bangBangControlled() {
        SimulatedSimpleSystem system = new SimulatedSimpleSystem(Motors.CIM, 100.0 * Units.POUND, 12.1 / 1.0, 3.0 * Units.INCH);
        system.setMaxCurrent(5.0);

        SimpleBangBangController controller = new SimpleBangBangController(Motors.CIM.getNominalVoltage());

        return new SimpleSimulation(system, controller);
    }

    public static SimpleSimulation proportionalControlled() {
        SimulatedSimpleSystem system = new SimulatedSimpleSystem(Motors.CIM, 100.0 * Units.POUND, 12.1 / 1.0, 3.0 * Units.INCH);
        system.setMaxCurrent(5.0);

        SimpleProportionalController controller = new SimpleProportionalController(1000.0);
        controller.setOutputRange(-Motors.CIM.getNominalVoltage(), Motors.CIM.getNominalVoltage());

        return new SimpleSimulation(system, controller);
    }

    @Override
    protected SimpleState getReference(double time, SimpleState current) {
        return new SimpleState(25.0 * Units.FOOT, 0.0 * Units.FOOT_PER_SECOND);
    }

    @Override
    protected void record(double time, SimpleState current, SimpleState reference, Double input) {
        recordedTimes.add(time);
        recordedPositions.add(current.getPosition() / Units.FOOT);
        recordedVelocities.add(current.getVelocity() / Units.FOOT_PER_SECOND);
        recordedReferences.add(reference.getPosition() / Units.FOOT);
        recordedVoltages.add(input);
    }

    @Override
    protected void makePlots(Plot plt) {
        plt.plot()
                .add(recordedTimes, recordedPositions);
        plt.plot()
                .add(recordedTimes, recordedReferences);
        plt.plot()
                .add(recordedTimes, recordedVelocities);
    }
}
