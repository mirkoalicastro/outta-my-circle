package com.badlogic.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Tile;

public class AndroidTile implements Tile {
    protected final Shader shader;
    private final AndroidPixmap pixmap;

    public AndroidTile(AndroidPixmap pixmap) {
        this.pixmap = pixmap;
        shader = new BitmapShader(pixmap.bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }
    @Override
    public int getWidth() {
        return pixmap.getWidth();
    }

    @Override
    public int getHeight() {
        return pixmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return pixmap.getFormat();
    }

    @Override
    public void dispose() {
        pixmap.dispose();
    }
}