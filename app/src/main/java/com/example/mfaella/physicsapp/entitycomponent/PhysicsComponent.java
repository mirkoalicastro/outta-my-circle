package com.example.mfaella.physicsapp.entitycomponent;

public abstract class PhysicsComponent extends Component {
    @Override
    public ComponentType type() { return ComponentType.Physics; }
}