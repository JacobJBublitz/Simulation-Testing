package com.github.kaboomboom3.simulationtesting;

public abstract class Controller<State, Input> {
    private State reference;

    public abstract Input calculate(State current);

    public State getReference() {
        return reference;
    }

    public void setReference(State reference) {
        this.reference = reference;
    }
}
