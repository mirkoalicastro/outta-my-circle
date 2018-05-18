package com.badlogic.androidgames.framework;

public interface Button {
    boolean inBounds(Input.TouchEvent event);
    int getWidth();
    int getHeight();
    int getX();
    int getY();
//    void setGraphics(Graphics graphics);
//    void setColor(int color);
//    void setPixmap(Pixmap pixmap);
//    void drawWithColor();
//    void drawWithPixmap();
    void draw(Graphics graphics, int color);
    void draw(Graphics graphics, Pixmap pixmap);
}
