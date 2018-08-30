package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidCircularButton implements Button {
    private final int x, y, radius;
    private boolean enabled;
    protected final Graphics graphics;

    public AndroidCircularButton(Graphics graphics, int x, int y, int radius) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean inBounds(Input.TouchEvent event) {
        return Math.pow(event.x-x,2)+Math.pow(event.y-y,2)<Math.pow(radius,2);
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public int getWidth() {
        return radius*2;
    }

    @Override
    public int getHeight() {
        return radius*2;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw(int color) {
        graphics.drawCircle(x,y,radius,color);
    }

    @Override
    public void draw(Pixmap pixmap) {
        graphics.drawPixmap(pixmap, x, y);
    }
}
