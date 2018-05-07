package com.example.mfaella.physicsapp.entitycomponent;

public class PositionComponent extends Component{
    public float x, y;

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }
}
