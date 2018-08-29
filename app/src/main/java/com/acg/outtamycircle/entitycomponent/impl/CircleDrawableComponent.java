package com.acg.outtamycircle.entitycomponent.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;


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
        graphics.drawCircle(x, y, radius, color);
    }
}
