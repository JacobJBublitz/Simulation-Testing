package com.github.kaboomboom3.simulationtesting.projectile;

import com.github.kaboomboom3.simulationtesting.ArrayUtils;
import com.github.kaboomboom3.simulationtesting.Controller;
import com.github.kaboomboom3.simulationtesting.Simulation;
import com.github.kaboomboom3.simulationtesting.Units;
import com.github.sh0nk.matplotlib4j.Plot;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectileSimulation extends Simulation<ProjectileState, Double> {
    private static final ProjectileSystemInformation SYSTEM_INFORMATION = new ProjectileSystemInformation(
            7 * Units.INCH,
            5 * Units.OUNCE,
            0.47
    );

    private static final double TARGET_HEIGHT = 8 * Units.FOOT + 2.25 * Units.INCH;

    private List<Vector2> recordedPositions = new ArrayList<>();

    private final double[] distances;
    private final double flywheelVelocity;
    private final double hoodAngle;

    private TargetHitState[] targetHitStates;

    public ProjectileSimulation(double[] distances, double flywheelVelocity, double hoodAngle) {
        super(
                new ProjectileSystem(SYSTEM_INFORMATION),
                new NullController(),
                new ProjectileState(
                        new Vector2(0.0, 22.0 * Units.INCH),
                        Vector2.fromAngle(Rotation2.fromRadians(hoodAngle)).scale(flywheelVelocity * Units.INCH),
                        Vector2.ZERO,
                        -0.1,
                        0.0
                )
        );
        this.distances = distances;
        targetHitStates = new TargetHitState[distances.length];
        this.flywheelVelocity = flywheelVelocity;
        this.hoodAngle = hoodAngle;
    }

    public static void main(String[] args) {
        double[] distances = Arrays.stream(ArrayUtils.arange(6, 21)).mapToDouble(i -> i * Units.FOOT).toArray();

        ProjectileSimulation[] bestSimulations = new ProjectileSimulation[distances.length];

        for (double flywheelVelocity = 2000.0 * Units.REVOLUTION_PER_MINUTE;
             flywheelVelocity <= 6000.0 * Units.REVOLUTION_PER_MINUTE;
             flywheelVelocity += 50.0 * Units.REVOLUTION_PER_MINUTE) {
            for (double hoodAngle = 26.0 * Units.DEGREE;
                 hoodAngle <= 66.0 * Units.DEGREE;
                 hoodAngle += 0.5 * Units.DEGREE) {
                var sim = new ProjectileSimulation(
                        distances,
                        flywheelVelocity,
                        hoodAngle
                );
                sim.setControlLoopPeriod(10.0 * Units.MILLISECOND);
                sim.setSimulationLoopPeriod(10.0 * Units.MILLISECOND);
                sim.setShouldShowGraph(false);
                sim.run();
                var targetHitStates = sim.getTargetHitStates();
                for (int i = 0; i < distances.length; i++) {
                    if (targetHitStates[i] != null) {
                        if (bestSimulations[i] == null) {
                            bestSimulations[i] = sim;
                        } else if (
                                Math.abs(targetHitStates[i].projectileState.getVelocity().y)
                                        <
                                        Math.abs(bestSimulations[i].getTargetHitStates()[i].projectileState.getVelocity().y)) {
                            bestSimulations[i] = sim;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < distances.length; i++) {
            if (bestSimulations[i] == null) {
                System.out.printf("Best (%.1f ft) -- None%n", distances[i] / Units.FOOT);
                continue;
            }

            System.out.printf("Best (%.1f ft) -- Hood angle: %.1f degrees, Flywheel Velocity: %.0f RPM, Ball Y Velocity: %.3f ft/s%n",
                    distances[i] / Units.FOOT,
                    bestSimulations[i].getHoodAngle() / Units.DEGREE,
                    bestSimulations[i].getFlywheelVelocity() / Units.REVOLUTION_PER_MINUTE,
                    bestSimulations[i].getTargetHitStates()[i].projectileState.getVelocity().y / Units.FOOT_PER_SECOND);
        }
    }

    public TargetHitState[] getTargetHitStates() {
        return targetHitStates;
    }

    public double[] getDistances() {
        return distances;
    }

    public double getFlywheelVelocity() {
        return flywheelVelocity;
    }

    public double getHoodAngle() {
        return hoodAngle;
    }

    public List<Vector2> getRecordedPositions() {
        return recordedPositions;
    }

    @Override
    protected ProjectileState getReference(double time, ProjectileState current) {
        return new ProjectileState(Vector2.ZERO, Vector2.ZERO, Vector2.ZERO, 0.0, 0.0);
    }

    @Override
    protected void record(double time, ProjectileState current, ProjectileState reference, Double ignored) {
        if (current.getPosition().y < 0.0)
            return;

        recordedPositions.add(current.getPosition());

        for (int i = 0; i < distances.length; i++) {
            double distanceToTarget = new Vector2(distances[i], TARGET_HEIGHT).subtract(current.getPosition()).length;
            if (distanceToTarget < 1.5 * Units.INCH) {
                if (targetHitStates[i] == null ||
                        distanceToTarget < reference.getPosition().subtract(targetHitStates[i].projectileState.getPosition()).length) {
                    targetHitStates[i] = new TargetHitState(time, distances[i], current);
                }
            }
        }
    }

    @Override
    protected void makePlots(Plot plt) { }

    private static class NullController extends Controller<ProjectileState, Double> {
        @Override
        public Double calculate(ProjectileState current) {
            return 0.0;
        }
    }

    public static class TargetHitState {
        public final double time;
        public final double distance;
        public final ProjectileState projectileState;

        public TargetHitState(double time, double distance, ProjectileState projectileState) {
            this.time = time;
            this.distance = distance;
            this.projectileState = projectileState;
        }
    }
}
