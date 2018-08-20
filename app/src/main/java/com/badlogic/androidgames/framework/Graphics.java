package com.badlogic.androidgames.framework;

public interface Graphics {
    enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }
    Tile newTile(String fileName, PixmapFormat format);

    Pixmap newPixmap(String fileName, PixmapFormat format);

    void clear(int color);

    void drawPixel(int x, int y, int color);

    void drawLine(int x, int y, int x2, int y2, int color);

    void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, int color);

    void drawRect(int x, int y, int width, int height, int color);

    void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawPixmap(Pixmap pixmap, int x, int y);

    void drawCircle(int x, int y, int radius, int color);

    boolean drawTile(Tile tile, int x, int y, int width, int height);

    int getWidth();

    int getHeight();
}
