package com.github.kaboomboom3.simulationtesting.arm;

public final class ArmState {
    private final double angle;
    private final double angularVelocity;

    public ArmState(double angle, double angularVelocity) {
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    public double getAngle() {
        return angle;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }
}
