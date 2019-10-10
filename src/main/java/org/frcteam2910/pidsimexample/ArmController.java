package org.frcteam2910.pidsimexample;

public final class ArmController extends Controller<ArmState, Double> {
    private final double angleGain;
    private final double angularVelocityGain;

    private final ArmSystemInformation systemInformation;

    private final double maximumCurrentDraw = 30.0;

    public ArmController(
            double angleGain,
            double angularVelocityGain,
            ArmSystemInformation systemInformation
    ) {
        this.angleGain = angleGain;
        this.angularVelocityGain = angularVelocityGain;
        this.systemInformation = systemInformation;
    }

    @Override
    public Double calculate(ArmState current) {
        double angleError = getReference().getAngle() - current.getAngle();
        double angularVelocityError = getReference().getAngularVelocity() - current.getAngularVelocity();

        double angleVoltage = angleGain * angleError;
        double angularVelocityVoltage = angularVelocityGain * angularVelocityError;

        // Gravity feedforward
        double feedforwardVoltage;
        {
            double gravitationalAcceleration = Math.cos(current.getAngle() / Units.RADIAN) * Constants.GRAVITATIONAL_ACCELERATION;
            double motorTorque = systemInformation.convertSystemAccelerationToMotorTorque(gravitationalAcceleration);
            double motorCurrent = systemInformation.getMotor().estimateCurrentDraw(motorTorque);
            feedforwardVoltage = systemInformation.getMotor().estimateVoltageInput(motorCurrent,
                    systemInformation.convertSystemVelocityToMotorVelocity(current.getAngularVelocity()));
        }

        double voltage = angleVoltage + angularVelocityVoltage + feedforwardVoltage;

        // Current limit
        double motorVelocity = systemInformation.convertSystemVelocityToMotorVelocity(current.getAngularVelocity());
        double estCurrentDraw = systemInformation.getMotor().estimateCurrentDraw(voltage, motorVelocity);
        voltage = systemInformation.getMotor().estimateVoltageInput(Math.max(-maximumCurrentDraw, Math.min(estCurrentDraw, maximumCurrentDraw)), motorVelocity);

        return Math.max(-systemInformation.getMotor().getNominalVoltage(), Math.min(voltage, systemInformation.getMotor().getNominalVoltage()));
    }
}
