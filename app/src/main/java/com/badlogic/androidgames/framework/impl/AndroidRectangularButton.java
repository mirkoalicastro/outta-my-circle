package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidRectangularButton implements Button {
    private final int x, y, width, height;

    public AndroidRectangularButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    public void draw(Graphics graphics, int color) {
        graphics.drawRect(x,y,width,height,color);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        graphics.drawPixmap(pixmap, x, y, 0,0, width, height);
    }
}
