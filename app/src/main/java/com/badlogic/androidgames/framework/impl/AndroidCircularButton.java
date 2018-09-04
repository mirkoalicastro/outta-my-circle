package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidCircularButton extends AndroidButton {
    protected int radius;

    /**
     * @param x        position of the center of the button
     * @param y        position of the center of the button
     */
    public AndroidCircularButton(Graphics graphics, int x, int y, int radius) {
        super(graphics, x, y, radius*2, radius*2);
        setRadius(radius);
    }

    @Override
    public boolean inBounds(Input.TouchEvent event) {
        return Math.pow(event.x-x,2)+Math.pow(event.y-y,2)<Math.pow(radius,2);
    }

    @Override
    public AndroidCircularButton setWidth(int width) {
        return setRadius(width / 2);
    }

    @Override
    public AndroidCircularButton setHeight(int height) {
        return setRadius(height / 2);
    }

    public AndroidCircularButton setRadius(int radius) {
        this.radius = radius;
        super.width = radius*2;
        super.height = radius*2;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawCircleBorder(x, y, radius, strokeWidth, strokeColor);
        if(color != null)
            graphics.drawCircle(x, y, radius, color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-radius, y-radius, width, height);
    }

}
