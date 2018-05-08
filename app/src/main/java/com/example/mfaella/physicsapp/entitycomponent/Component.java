package com.example.mfaella.physicsapp.entitycomponent;

public abstract class Component {
    public static enum Type {Physics, Drawable, Controllable, Position}
    protected Entity owner;
    public abstract Type type();

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}