package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Canvas;

public abstract class DrawableComponent extends Component {
    @Override
    public ComponentType type() { return ComponentType.Drawable; }
    public abstract void draw(Canvas canvas);
}