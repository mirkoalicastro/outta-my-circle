package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class AndroidButton implements Button {
    protected int x, y, width, height;
    protected final Graphics graphics;
    private boolean enabled = true;
    protected Integer color;
    protected Pixmap pixmap;
    protected int strokeWidth;
    protected Integer strokeColor;
    protected Effect effect;

    public AndroidButton(Graphics graphics, int x, int y, int width, int height) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public AndroidButton setEffect(Effect effect) {
        this.effect = effect;
        return this;
    }

    public Effect getEffect() {
        return effect;
    }

    public AndroidButton setX(int x) {
        this.x = x;
        return this;
    }

    public AndroidButton setY(int y) {
        this.y = y;
        return this;
    }

    public AndroidButton setWidth(int width) {
        this.width = width;
        return this;
    }

    public AndroidButton setHeight(int height) {
        this.height = height;
        return this;
    }

    public AndroidButton setColor(Integer color) {
        this.color = color;
        return this;
    }

    public Integer getColor() {
        return color;
    }

    public AndroidButton setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        return this;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public AndroidButton setStroke(int strokeWidth, Integer strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public Integer getStrokeColor() {
        return strokeColor;
    }

    public AndroidButton(Graphics graphics, int x, int y, int width, int height, Integer color, Pixmap pixmap, int strokeWidth, Integer strokeColor, Effect effect) {
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

    public abstract boolean inBounds(Input.TouchEvent event);

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

}
