package com.acg.outtamycircle.entitycomponent;

public abstract class Component {

    public enum Type {Physics, Drawable, Attack}
    protected Entity owner;
    public abstract Type type();

    public Component setOwner(Entity owner) {
        this.owner = owner;
        return this;
    }

    public Entity getOwner() {
        return owner;
    }
}