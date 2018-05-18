package com.example.mfaella.physicsapp.entitycomponent.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

public class CircleDrawableComponent extends DrawableComponent {
    private int radius;

    public CircleDrawableComponent(Graphics graphics, int radius){
        super(graphics);
        this.radius = radius;
    }

    @Override
    public void drawColor() {
        PositionComponent pos = (PositionComponent) owner.getComponent(Type.Position);

        graphics.drawCircle(pos.x, pos.y, radius, color);
    }

    @Override
    public void drawPixmap() {
        PositionComponent pos = (PositionComponent) owner.getComponent(Type.Position);

        graphics.drawPixmap(pixmap, pos.x, pos.y);
    }
}
