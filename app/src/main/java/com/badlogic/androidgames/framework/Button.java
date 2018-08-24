package com.badlogic.androidgames.framework;

public interface Button {
    boolean inBounds(Input.TouchEvent event);
    int getWidth();
    int getHeight();
    int getX();
    int getY();
    void draw(Graphics graphics, int color);
    void draw(Graphics graphics, Pixmap pixmap);
}
