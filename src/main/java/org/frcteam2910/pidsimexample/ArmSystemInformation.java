package org.frcteam2910.pidsimexample;

public final class ArmSystemInformation {
    private final BrushedDcMotor motor;
    private final double momentOfInertia;
    private final double gearRatio;

    public ArmSystemInformation(BrushedDcMotor motor, double momentOfInertia, double gearRatio) {
        this.motor = motor;
        this.momentOfInertia = momentOfInertia;
        this.gearRatio = gearRatio;
    }

    public BrushedDcMotor getMotor() {
        return motor;
    }

    public double getMomentOfInertia() {
        return momentOfInertia;
    }

    public double getGearRatio() {
        return gearRatio;
    }

    public double convertMotorVelocityToSystemVelocity(double motorVelocity) {
        return motorVelocity / gearRatio;
    }

    public double convertSystemVelocityToMotorVelocity(double systemVelocity) {
        return systemVelocity * gearRatio;
    }

    public double convertMotorTorqueToSystemAcceleration(double motorTorque) {
        double pulleyTorque = motorTorque * gearRatio;
        return pulleyTorque / momentOfInertia;
    }

    public double convertSystemAccelerationToMotorTorque(double systemAcceleration) {
        double pulleyTorque = systemAcceleration * momentOfInertia;
        return pulleyTorque / gearRatio;
    }
}
