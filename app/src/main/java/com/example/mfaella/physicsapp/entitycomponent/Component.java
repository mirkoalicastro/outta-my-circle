package com.example.mfaella.physicsapp.entitycomponent;

public abstract class Component {
    protected Entity owner;
    public abstract ComponentType type();

    public Component(Entity owner) {
        this.owner = owner;
    }
}