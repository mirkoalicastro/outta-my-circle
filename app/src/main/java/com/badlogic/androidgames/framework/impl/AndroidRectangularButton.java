package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidRectangularButton implements Button {
    protected final int x, y, width, height;
    protected final Graphics graphics;
    private boolean enabled;
    protected Integer color;
    protected Pixmap pixmap;
    protected int strokeWidth;
    protected Integer strokeColor;
    protected Effect effect;

    public AndroidRectangularButton(Graphics graphics, Effect effect, int x, int y, int width, int height, Integer color, Pixmap pixmap, int strokeWidth, Integer strokeColor) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
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
        if(color != null)
            graphics.drawRect(x,y,width,height,color);
        if(pixmap != null)
            graphics.drawPixmap(pixmap,x,y,width,height);
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawRectBorder(x,y,width,height,strokeWidth,strokeColor);
        if(effect != null)
            graphics.drawEffect(effect, x, y, getWidth(), getHeight());
    }

}
