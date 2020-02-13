package com.github.kaboomboom3.simulationtesting.projectile;

import com.github.kaboomboom3.simulationtesting.Constants;
import com.github.kaboomboom3.simulationtesting.SimulatedSystem;
import org.frcteam2910.common.math.Vector2;

public class ProjectileSystem extends SimulatedSystem<ProjectileState, Double> {
    private final ProjectileSystemInformation systemInformation;
    private final boolean enableDrag;
    private final boolean enableMagnusEffect;

    public ProjectileSystem(ProjectileSystemInformation systemInformation) {
        this(systemInformation, true, true);
    }

    public ProjectileSystem(ProjectileSystemInformation systemInformation, boolean enableDrag, boolean enableMagnusEffect) {
        this.systemInformation = systemInformation;
        this.enableDrag = enableDrag;
        this.enableMagnusEffect = enableMagnusEffect;
    }

    @Override
    public ProjectileState simulate(ProjectileState state, Double ignored, double dt) {
        Vector2 velocity = state.getVelocity();
        Vector2 acceleration = new Vector2(0, -Constants.GRAVITATIONAL_ACCELERATION);

        double angularAcceleration = 0.0;

        if (enableDrag) {
            double drag = 0.5
                    * systemInformation.getDragCoefficient()
                    * Constants.AIR_DENSITY
                    * (Math.PI * Math.pow(systemInformation.getDiameter() / 2.0, 2.0));
            acceleration = acceleration.add(
                    velocity.inverse().multiply(velocity).scale(drag / systemInformation.getMass())
            );
        }
        if (enableMagnusEffect) {
            double lift = 4.0 / 3.0
                    * (4.0 * Math.pow(Math.PI, 2.0)
                    * Math.pow(systemInformation.getDiameter() / 2.0, 3.0)
                    * state.getAngularVelocity()
                    * Constants.AIR_DENSITY);
            acceleration = acceleration.add(
                    velocity.inverse().scale(lift / systemInformation.getMass())
            );
        }

        return new ProjectileState(
                state.getPosition().add(velocity.scale(dt)),
                state.getVelocity().add(acceleration.scale(dt)),
                acceleration,
                state.getAngularVelocity() + angularAcceleration * dt,
                angularAcceleration
        );
    }
}
