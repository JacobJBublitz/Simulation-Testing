package com.github.kaboomboom3.simulationtesting.elevator;

import com.github.kaboomboom3.simulationtesting.BrushedDcMotor;

public final class ElevatorSystemInformation {
    private final BrushedDcMotor motor;
    private final double mass;
    private final double gearRatio;
    private final double pulleyRadius;

    public ElevatorSystemInformation(BrushedDcMotor motor, double mass, double gearRatio, double pulleyRadius) {
        this.motor = motor;
        this.mass = mass;
        this.gearRatio = gearRatio;
        this.pulleyRadius = pulleyRadius;
    }

    public BrushedDcMotor getMotor() {
        return motor;
    }

    public double getMass() {
        return mass;
    }

    public double getGearRatio() {
        return gearRatio;
    }

    public double getPulleyRadius() {
        return pulleyRadius;
    }

    public double convertMotorVelocityToSystemVelocity(double motorVelocity) {
        double pulleyVelocity = motorVelocity / gearRatio; // Vp = Vm / G
        return pulleyVelocity * pulleyRadius; // v = Vp * r
    }

    public double convertSystemVelocityToMotorVelocity(double systemVelocity) {
        double pulleyVelocity = systemVelocity / pulleyRadius; // Vp = v / r
        return pulleyVelocity * gearRatio; // Vm = Vp * G
    }

    public double convertMotorTorqueToSystemAcceleration(double motorTorque) {
        double pulleyTorque = motorTorque * gearRatio; // Tp = Tm * G
        double force = pulleyTorque / pulleyRadius; // F = Tp / r
        return force / mass; // a = F/m
    }

    public double convertSystemAccelerationToMotorTorque(double systemAcceleration) {
        double force = mass * systemAcceleration; // F = ma
        double pulleyTorque = force * pulleyRadius; // Tp = Fr
        return pulleyTorque / gearRatio; // Tm = Tp / G
    }
}
