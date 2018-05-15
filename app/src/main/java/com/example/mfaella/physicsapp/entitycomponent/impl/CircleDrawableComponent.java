package com.example.mfaella.physicsapp.entitycomponent.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

public class CircleDrawableComponent extends DrawableComponent {
    private float x, y, radius;
    private Paint paint;

    public CircleDrawableComponent(Paint paint){
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        PositionComponent pos = (PositionComponent)
                owner.getComponent(Type.Position);
        x = pos.x;
        y = pos.y;

        radius = ((CirclePhysicsComponent)owner.getComponent(Type.Physics)).radius;

        canvas.drawCircle(x, y, radius, paint);
    }
}
