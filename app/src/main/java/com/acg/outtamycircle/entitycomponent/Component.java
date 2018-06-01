package com.acg.outtamycircle.entitycomponent;

public abstract class Component {
    public enum Type {Physics, Drawable, Controllable, Position}
    protected Entity owner;
    public abstract Type type();

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}