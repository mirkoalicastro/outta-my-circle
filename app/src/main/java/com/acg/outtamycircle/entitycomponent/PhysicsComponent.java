package com.acg.outtamycircle.entitycomponent;

public abstract class PhysicsComponent extends Component {
    @Override
    public Component.Type type() { return Component.Type.Physics; }

    //TODO se l'oggetto fisico è un oggetto statico, non mi pare abbia senso applicargli una forza
    public abstract void applyForce(float x, float y);

    public abstract float getX();

    public abstract float getY();
}