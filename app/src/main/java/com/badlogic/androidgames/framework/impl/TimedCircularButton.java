package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;

import java.util.Calendar;

public class TimedCircularButton extends AndroidCircularButton {
    private long millis;
    private long validTime;

    public TimedCircularButton(long millis, int x, int y, int radius) {
        super(x, y, radius);
        setMillis(millis);
    }

    public void setMillis(long millis) {
        if(millis < 0)
            throw new IllegalArgumentException("Time cannot be less than zero");
        this.millis = millis;
    }

    /*
    *
    *  This method also update next valid time, if it is now valid
    *
     */
    public boolean isActive() {
        long tmp;
        if((tmp=Calendar.getInstance().getTimeInMillis()) > validTime) {
            validTime = tmp+millis;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics graphics, int color) {
        super.draw(graphics, color);
        graphics.drawArc(getX()-getRadius(), getY()-getRadius(), getX()+getRadius(), getY()+getRadius(), 270, calculateProgress()*360, true, Color.RED);
    }

    private float calculateProgress() {
        long tmp = validTime - Calendar.getInstance().getTimeInMillis();
        if(tmp < 0)
            return 0;
        return (float)tmp/(float)millis;
    }
}
