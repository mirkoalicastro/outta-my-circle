package com.example.mfaella.physicsapp.entitycomponent;

public abstract class PhysicsComponent extends Component {
    @Override
    public Component.Type type() { return Component.Type.Physics; }
}