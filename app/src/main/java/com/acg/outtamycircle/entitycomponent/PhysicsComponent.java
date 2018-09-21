package com.acg.outtamycircle.entitycomponent;

public abstract class PhysicsComponent extends Component {
    @Override
    public Component.Type type() { return Component.Type.Physics; }

    public abstract void applyForce(float x, float y);

    public abstract float getX();

    public abstract float getY();

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract void delete();
}