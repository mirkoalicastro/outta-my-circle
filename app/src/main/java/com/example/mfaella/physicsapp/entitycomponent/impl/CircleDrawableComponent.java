package com.example.mfaella.physicsapp.entitycomponent.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

public class CircleDrawableComponent extends DrawableComponent {
    private float x, y, radius;
    @Override
    public void draw(Canvas canvas, Paint paint) {
        PositionComponent pos = (PositionComponent)
                owner.getComponent(Type.Position);
        x = pos.x;
        y = pos.y;

        radius = ((CirclePhysicsComponent)owner.getComponent(Type.Physics)).radius;

        canvas.drawCircle(x, y, radius, paint);

    }
}
