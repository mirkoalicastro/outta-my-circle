package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Tile;

public class AndroidGraphics implements Graphics {
    private final AssetManager assets;
    private final Bitmap frameBuffer;
    private final Canvas canvas;
    private final Paint paint;
    private final Rect srcRect;
    private final Rect dstRect;
    private final Options options;
    private final RectF rectF;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        this.srcRect = new Rect();
        this.dstRect = new Rect();
        this.rectF = new RectF();
        this.options = new Options();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in,null,options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"+ fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"+ fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    public Tile newTile(String fileName, PixmapFormat format) {
        return new AndroidTile((AndroidPixmap) newPixmap(fileName, format));
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, int color) {
        paint.setColor(color);
        rectF.left = left;
        rectF.top = top;
        rectF.right  = right;
        rectF.bottom = bottom;

        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,null);
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public void drawCircle(int x, int y, int radius, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(x,y,radius,paint);
    }

    @Override
    public void drawTile(Tile tile, int x, int y, int width, int height) {
        paint.setShader(((AndroidTile)tile).shader);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
        paint.setShader(null);
    }

    @Override
    public void drawText(String text, int x, int y, int fontSize, int color) {
        paint.setTextSize(fontSize);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

}
