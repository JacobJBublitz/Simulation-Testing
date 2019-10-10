package org.frcteam2910.pidsimexample;

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
