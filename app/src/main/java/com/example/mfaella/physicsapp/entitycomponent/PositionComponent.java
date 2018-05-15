package com.example.mfaella.physicsapp.entitycomponent;

public class PositionComponent extends Component {
    public float x, y;

    public PositionComponent(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public Component.Type type() {
        return Component.Type.Position;
    }
}
