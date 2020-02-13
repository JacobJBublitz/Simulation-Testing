package com.github.kaboomboom3.simulationtesting.projectile;

public class ProjectileSystemInformation {
    private final double diameter;
    private final double mass;
    private final double dragCoefficient;

    public ProjectileSystemInformation(double diameter, double mass, double dragCoefficient) {
        this.diameter = diameter;
        this.mass = mass;
        this.dragCoefficient = dragCoefficient;
    }

    public double getDiameter() {
        return diameter;
    }

    public double getMass() {
        return mass;
    }

    public double getDragCoefficient() {
        return dragCoefficient;
    }
}
