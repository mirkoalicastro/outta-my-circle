package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DrawableComponent extends Component {
    @Override
    public Component.Type type() { return Component.Type.Drawable; }
    public abstract void draw(Canvas canvas, Paint paint);
}