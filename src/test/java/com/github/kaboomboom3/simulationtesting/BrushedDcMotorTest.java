package com.github.kaboomboom3.simulationtesting;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class BrushedDcMotorTest {
    private static final double ALLOWABLE_CURRENT_ERROR = 10.0e-3; // 10 mA
    private static final double ALLOWABLE_TORQUE_ERROR = 100.0e-3; // 100 mNm

    private static Stream<List<Double>> downloadMotorData(String motorDataUrl) throws IOException {
        return downloadMotorData(new URL(motorDataUrl));
    }

    private static Stream<List<Double>> downloadMotorData(URL motorDataUrl) throws IOException {
        InputStream in = motorDataUrl.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Stream<String> lines = reader.lines().skip(1);

        System.out.println(lines.findFirst().orElse(""));

        return reader.lines().skip(1)
                .map(line -> Arrays.stream(line.split(",")).map(Double::parseDouble).collect(Collectors.toList()));
    }

    private static void testMotor(BrushedDcMotor motor, Stream<List<Double>> motorData) {
        motorData.forEach(data -> {
            double velocity = data.get(0) * Units.REVOLUTION_PER_MINUTE;
            double actualTorqueOutput = data.get(1);
            double actualCurrentDraw = data.get(2);

            double estimatedCurrentDraw = motor.estimateCurrentDraw(motor.getNominalVoltage(), velocity);
            double estimatedTorqueOutput = motor.estimateTorqueOutput(actualCurrentDraw);

            assertEquals("Estimated current draw does not match actual current draw",
                    actualCurrentDraw, estimatedCurrentDraw, ALLOWABLE_CURRENT_ERROR);
            assertEquals("Estimated torque output does not match actual torque output",
                    actualTorqueOutput, estimatedTorqueOutput, ALLOWABLE_TORQUE_ERROR);
        });
    }

    @Test
    public void cimSimulationTest() {
        try (Stream<List<Double>> motorData =
                     downloadMotorData("https://content.vexrobotics.com/motors/217-2000-cim/cim-motor-curve-data-20151104.csv")) {
            testMotor(Motors.CIM, motorData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void miniCimSimulationTest() {
        try (Stream<List<Double>> motorData =
                     downloadMotorData("https://content.vexrobotics.com/motors/217-3371-mini-cim/mini-cim-motor-curve-data-20151207.csv")) {
            testMotor(Motors.MINI_CIM, motorData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
