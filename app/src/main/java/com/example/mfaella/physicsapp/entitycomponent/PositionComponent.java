package com.example.mfaella.physicsapp.entitycomponent;

public class PositionComponent extends Component {
    public float x, y;

    @Override
    public Component.Type type() {
        return Component.Type.Position;
    }
}
