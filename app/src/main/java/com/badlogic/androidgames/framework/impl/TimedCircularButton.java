package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.Calendar;

public class TimedCircularButton extends AndroidCircularButton {
    private long millis;
    private long validTime;
    protected Integer secondaryColor;
    protected Pixmap primaryPixmap, secondaryPixmap;

    public TimedCircularButton(Graphics graphics, Effect effect, long millis, int x, int y, int radius, Integer activeColor, Integer secondaryColor, Pixmap primaryPixmap, Pixmap secondaryPixmap, int strokeWidth, Integer strokeColor) {
        super(graphics, effect, x, y, radius, activeColor, null, strokeWidth, strokeColor);
        this.secondaryColor = secondaryColor;
        this.primaryPixmap = primaryPixmap;
        this.secondaryPixmap = secondaryPixmap;
        setMillis(millis);
    }

    public void setMillis(long millis) {
        if(millis < 0)
            throw new IllegalArgumentException("Time cannot be less than zero");
        this.millis = millis;
    }

    public void resetTime() {
        validTime = System.currentTimeMillis()+millis;
    }

    @Override
    public boolean isEnabled() {
        return validTime != -1 && System.currentTimeMillis() >= validTime;
    }

    @Override
    public void enable(boolean enabled) {
        if(enabled)
            resetTime();
        else
            validTime = -1;
    }

    private void drawPixmap(Pixmap pixmap) {
        graphics.drawPixmap(pixmap, x-radius, y-radius, radius*2,radius*2);
    }

    @Override
    public void draw() {
        super.draw();
        graphics.drawArc(x-radius, y-radius, x+radius, y+radius, 270, calculateProgress()*360, true, secondaryColor);
        if(isEnabled())
            drawPixmap(primaryPixmap);
        else
            drawPixmap(secondaryPixmap);
    }

    private float calculateProgress() {
        if(validTime == -1)
            return 1;
        long tmp = validTime - System.currentTimeMillis();
        if(tmp < 0)
            return 0;
        return (float)tmp/(float)millis;
    }
}
