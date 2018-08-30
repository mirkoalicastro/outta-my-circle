package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.Calendar;

public class TimedCircularButton extends AndroidCircularButton {
    private long millis;
    private long validTime;

    public TimedCircularButton(Graphics graphics, long millis, int x, int y, int radius) {
        super(graphics, x, y, radius);
        setMillis(millis);
    }

    public void setMillis(long millis) {
        if(millis < 0)
            throw new IllegalArgumentException("Time cannot be less than zero");
        this.millis = millis;
    }

    public void resetTime() {
        validTime = Calendar.getInstance().getTimeInMillis()+millis;
    }

    @Override
    public boolean isEnabled() {
        return validTime != -1 && Calendar.getInstance().getTimeInMillis() >= validTime;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(enabled)
            resetTime();
        else
            validTime = -1;
    }

    @Override
    public void draw(Pixmap pixmap) {
        draw(Color.GREEN, Color.RED, pixmap);
    }

    public void draw(int color1, int color2, Pixmap pixmap) {
        draw(color1, color2);
        super.draw(pixmap);
    }

    public void draw(int color1, int color2, Pixmap pixmap, int strokeWidth, int colorStroke) {
        draw(color1, color2, pixmap);
        super.drawStroke(strokeWidth, colorStroke);
    }

    @Override
    public void draw(int color) {
        draw(color, Color.RED);
    }

    public void draw(int color1, int color2, int strokeWidth, int colorStroke) {
        draw(color1, color2);
        super.drawStroke(strokeWidth, colorStroke);
    }

    public void draw(int color1, int color2) {
        super.draw(color1);
        graphics.drawArc(getX()-getRadius(), getY()-getRadius(), getX()+getRadius(), getY()+getRadius(), 270, calculateProgress()*360, true, color2);
    }

    private float calculateProgress() {
        if(validTime == -1)
            return 1;
        long tmp = validTime - Calendar.getInstance().getTimeInMillis();
        if(tmp < 0)
            return 0;
        return (float)tmp/(float)millis;
    }
}
