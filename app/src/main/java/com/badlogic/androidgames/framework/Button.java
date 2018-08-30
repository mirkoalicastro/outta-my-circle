package com.badlogic.androidgames.framework;

public interface Button {
    boolean isEnabled();
    boolean inBounds(Input.TouchEvent event);
    int getWidth();
    int getHeight();
    int getX();
    int getY();
    void setEnabled(boolean enabled);
    void draw(int color);
    void draw(Pixmap pixmap);
}
