package org.frcteam2910.pidsimexample;

public final class SimulatedElevatorSystem extends SimulatedSystem<ElevatorState, Double> {
    private final ElevatorSystemInformation systemInformation;

    public SimulatedElevatorSystem(ElevatorSystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }

    @Override
    public ElevatorState simulate(ElevatorState state, Double voltage, double dt) {
        double motorVelocity = systemInformation.convertSystemVelocityToMotorVelocity(state.getVelocity());

        double motorTorque = systemInformation.getMotor().estimateTorqueOutput(voltage, motorVelocity);
        double acceleration = systemInformation.convertMotorTorqueToSystemAcceleration(motorTorque);
        acceleration += -Constants.GRAVITATIONAL_ACCELERATION;

        return new ElevatorState(
                state.getPosition() + state.getVelocity() * dt,
                state.getVelocity() + acceleration * dt
        );
    }
}
