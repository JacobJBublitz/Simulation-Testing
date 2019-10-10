package com.github.kaboomboom3.simulationtesting.elevator;

import com.github.kaboomboom3.simulationtesting.Controller;

import static com.github.kaboomboom3.simulationtesting.Constants.GRAVITATIONAL_ACCELERATION;

public final class ElevatorController extends Controller<ElevatorState, Double> {
    private final double positionGain;
    private final double velocityGain;

    private final ElevatorSystemInformation systemInformation;

    private final double feedforwardVoltage;
    private final double maximumCurrentDraw = 30.0;

    public ElevatorController(
            double positionGain,
            double velocityGain,
            ElevatorSystemInformation systemInformation
    ) {
        this.positionGain = positionGain;
        this.velocityGain = velocityGain;
        this.systemInformation = systemInformation;

        double motorTorque = systemInformation.convertSystemAccelerationToMotorTorque(GRAVITATIONAL_ACCELERATION);
        double motorCurrent = systemInformation.getMotor().estimateCurrentDraw(motorTorque);
        feedforwardVoltage = systemInformation.getMotor().estimateVoltageInput(motorCurrent, 0.0);
    }

    @Override
    public Double calculate(ElevatorState current) {
        double positionError = getReference().getPosition() - current.getPosition();
        double velocityError = getReference().getVelocity() - current.getVelocity();

        double positionVoltage = positionGain * positionError;
        double velocityVoltage = velocityGain * velocityError;

        double voltage = positionVoltage + velocityVoltage + feedforwardVoltage;

        // Current limit
        double motorVelocity = systemInformation.convertSystemVelocityToMotorVelocity(current.getVelocity());
        double estCurrentDraw = systemInformation.getMotor().estimateCurrentDraw(voltage, motorVelocity);
        voltage = systemInformation.getMotor().estimateVoltageInput(Math.max(-maximumCurrentDraw, Math.min(estCurrentDraw, maximumCurrentDraw)), motorVelocity);

        return Math.max(-systemInformation.getMotor().getNominalVoltage(), Math.min(voltage, systemInformation.getMotor().getNominalVoltage()));
    }
}
