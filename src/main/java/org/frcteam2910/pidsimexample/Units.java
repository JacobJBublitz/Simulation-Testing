package org.frcteam2910.pidsimexample;

public class Units {
    public static final double SECOND = 1.0;
    public static final double MILLISECOND = 1.0e-3 * SECOND;
    public static final double MICROSECOND = 1.0e-6 * SECOND;
    public static final double MINUTE = 60.0 * SECOND;

    public static final double METER = 1.0;
    public static final double CENTIMETER = 1.0e-3 * METER;
    public static final double INCH = 2.54 * CENTIMETER;
    public static final double FOOT = 12.0 * INCH;

    public static final double METER_PER_SECOND = METER / SECOND;
    public static final double METER_PER_SECOND2 = METER_PER_SECOND / SECOND;
    public static final double FOOT_PER_SECOND = FOOT / SECOND;
    public static final double FOOT_PER_SECOND2 = FOOT_PER_SECOND / SECOND;

    public static final double RADIAN = 1.0;
    public static final double DEGREE = Math.PI * RADIAN / 180.0;
    public static final double REVOLUTION = 2.0 * Math.PI * RADIAN;

    public static final double RADIAN_PER_SECOND = RADIAN / SECOND;
    public static final double DEGREE_PER_SECOND = DEGREE / SECOND;
    public static final double REVOLUTION_PER_MINUTE = REVOLUTION / MINUTE;

    public static final double KILOGRAM = 1.0;
    public static final double GRAM = 1.0e-3 * KILOGRAM;
    public static final double POUND = 453.59237 * GRAM;
}
