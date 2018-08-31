package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidCircularButton implements Button {
    protected final int x, y, radius;
    protected final Graphics graphics;
    private boolean enabled;
    protected Integer color;
    protected Pixmap pixmap;
    protected int strokeWidth;
    protected Integer strokeColor;
    protected Effect effect;

    /**
     * @param graphics
     * @param x        position of the center of the button
     * @param y        position of the center of the button
     * @param radius
     */
    public AndroidCircularButton(Graphics graphics, Effect effect, int x, int y, int radius, Integer color, Pixmap pixmap, int strokeWidth, Integer strokeColor) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.pixmap = pixmap;
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.effect = effect;
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
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawCircleBorder(x,y,radius,strokeWidth,strokeColor);
        if(color != null)
            graphics.drawCircle(x,y,radius,color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, getWidth(), getHeight());
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-radius, y-radius, radius*2,radius*2);
    }

}
