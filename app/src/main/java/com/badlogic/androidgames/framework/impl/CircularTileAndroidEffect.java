package com.badlogic.androidgames.framework.impl;

import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;

public class CircularTileAndroidEffect extends TileAndroidEffect {

    public CircularTileAndroidEffect(AndroidPixmap pixmap) {
        super(pixmap);
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        paint.setShader(shader);
        canvas.drawCircle(x, y, width/2, paint);
        paint.setShader(null);
    }
}
