package com.acg.outtamycircle.entitycomponent.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.PositionComponent;


public class CircleDrawableComponent extends DrawableComponent {
    private int radius, color;

    public CircleDrawableComponent(Graphics graphics, int radius){
        super(graphics);
        this.radius = radius;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(){
        PositionComponent pos = (PositionComponent) owner.getComponent(Type.Position);

        graphics.drawCircle(pos.x, pos.y, radius, color);

        //graphics.drawCircle(pos.x, pos.y, radius, color);
    }
}
