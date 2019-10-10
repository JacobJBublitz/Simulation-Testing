package org.frcteam2910.pidsimexample;

public final class SimulatedArmSystem extends SimulatedSystem<ArmState, Double> {
    private final ArmSystemInformation systemInformation;

    public SimulatedArmSystem(ArmSystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }

    @Override
    public ArmState simulate(ArmState state, Double voltage, double dt) {
        double motorVelocity = systemInformation.convertSystemVelocityToMotorVelocity(state.getAngularVelocity());

        double motorTorque = systemInformation.getMotor().estimateTorqueOutput(voltage, motorVelocity);
        double angularAcceleration = systemInformation.convertMotorTorqueToSystemAcceleration(motorTorque);
        angularAcceleration += Math.cos(state.getAngle() / Units.RADIAN) * -Constants.GRAVITATIONAL_ACCELERATION;

        return new ArmState(
                state.getAngle() + state.getAngularVelocity() * dt,
                state.getAngularVelocity() + angularAcceleration * dt
        );
    }
}
