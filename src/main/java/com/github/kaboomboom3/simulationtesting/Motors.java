package com.github.kaboomboom3.simulationtesting;

public final class Motors {
    /**
     * A CIM motor
     * <p>
     * Motor data was retrieved from <a href="https://motors.vex.com/vexpro-motors/cim-motor">VEX's motor data</a>.
     */
    public static final BrushedDcMotor CIM = new BrushedDcMotor(
            12.0,
            5330.0 * Units.REVOLUTION_PER_MINUTE,
            2.7,
            2.413,
            131.055
    );

    /**
     * A Mini CIM motor
     * <p>
     * Motor data was retrieved from <a href="https://motors.vex.com/vexpro-motors/mini-cim-motor">VEX's motor data</a>.
     */
    public static final BrushedDcMotor MINI_CIM = new BrushedDcMotor(
            12.0,
            5839.541 * Units.REVOLUTION_PER_MINUTE,
            2.986,
            1.409,
            89.391
    );

    private Motors() {
        throw new UnsupportedOperationException();
    }
}
