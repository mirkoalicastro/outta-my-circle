package com.acg.outtamycircle.entitycomponent;

public abstract class PhysicsComponent extends Component {
    @Override
    public Component.Type type() { return Component.Type.Physics; }
}