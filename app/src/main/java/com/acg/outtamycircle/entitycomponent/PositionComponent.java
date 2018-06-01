package com.acg.outtamycircle.entitycomponent;

public class PositionComponent extends Component {
    public int x, y;

    public PositionComponent(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public Component.Type type() {
        return Component.Type.Position;
    }
}
