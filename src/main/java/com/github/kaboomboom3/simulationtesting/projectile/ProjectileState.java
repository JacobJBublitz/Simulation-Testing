package com.github.kaboomboom3.simulationtesting.projectile;

import org.frcteam2910.common.math.Vector2;

public class ProjectileState {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 acceleration;
    private final double angularVelocity;
    private final double angularAcceleration;

    public ProjectileState(Vector2 position, Vector2 velocity, Vector2 acceleration, double angularVelocity, double angularAcceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }
}
