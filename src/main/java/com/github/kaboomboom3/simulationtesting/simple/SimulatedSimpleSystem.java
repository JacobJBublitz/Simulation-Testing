package com.github.kaboomboom3.simulationtesting.simple;

import com.github.kaboomboom3.simulationtesting.BrushedDcMotor;
import com.github.kaboomboom3.simulationtesting.SimulatedSystem;

public final class SimulatedSimpleSystem extends SimulatedSystem<SimpleState, Double> {
    private final BrushedDcMotor motor;
    private final double mass;
    private final double gearRatio;
    private final double pulleyRadius;

    private double maxCurrent = Double.POSITIVE_INFINITY;

    public SimulatedSimpleSystem(BrushedDcMotor motor, double mass, double gearRatio, double pulleyRadius) {
        this.motor = motor;
        this.mass = mass;
        this.gearRatio = gearRatio;
        this.pulleyRadius = pulleyRadius;
    }

    public void setMaxCurrent(double maxCurrent) {
        this.maxCurrent = maxCurrent;
    }

    @Override
    public SimpleState simulate(SimpleState current, Double voltage, double dt) {
        double motorVelocity = current.getVelocity() / pulleyRadius * gearRatio;

        double motorTorque = motor.estimateTorqueOutput(voltage, motorVelocity);

        // Current limiting
        double motorCurrent = motor.estimateCurrentDraw(motorTorque);
        if (Math.abs(motorCurrent) > maxCurrent) {
            motorCurrent = Math.copySign(maxCurrent, motorCurrent);
            motorTorque = motor.estimateTorqueOutput(motorCurrent);
        }

        double acceleration = motorTorque * gearRatio / pulleyRadius / mass;
        double velocity = current.getVelocity() + acceleration * dt;
        double position = current.getPosition() + velocity * dt;

        return new SimpleState(position, velocity);
    }
}
