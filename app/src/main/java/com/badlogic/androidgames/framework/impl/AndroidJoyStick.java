package com.badlogic.androidgames.framework.impl;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.JoyStick;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoyStick extends AndroidCircularButton implements JoyStick {
    private final Input input;
    private int x, y;
    private final List<Input.TouchEvent> buffer;
    private final int radius;
    private int pointer = -1;

    public AndroidJoyStick(Input input, int x, int y, int radius) {
        super(x, y, radius);
        this.radius = radius;
        this.input = input;
        buffer = new LinkedList<>();
    }

    @Override
    public List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events) {
        for (Input.TouchEvent event : events) {
            if(event.type == Input.TouchEvent.TOUCH_DOWN)
                if(Math.pow(event.x-getX(),2)+Math.pow(event.y-getY(),2)<Math.pow(radius*2,2))
                    pointer = event.pointer;
            if(event.type == Input.TouchEvent.TOUCH_UP && pointer == event.pointer) {
                pointer = -1;
                x = 0;
                y = 0;
            }
            if (event.pointer == pointer) {
                x = event.x - getX();
                y = getY() - event.y;
                int tmpx = Math.abs((int)((radius)*Math.cos(Math.atan2(y,x))));
                int tmpy = Math.abs((int)((radius)*Math.sin(Math.atan2(y,x))));
                x = Math.max(Math.min(tmpx, x), tmpx*-1);
                y = Math.max(Math.min(tmpy, y), tmpy*-1);
                buffer.add(event);
            }
        }
        if(!buffer.isEmpty())
            Log.d("JoyStickCore", "Angolo: " + getAngle());
        events.removeAll(buffer);
        buffer.clear();
        return events;
    }

    @Override
    public double getAngle() {
        getDistance(); return Math.toDegrees(Math.atan2(y,x));
    }

    public double getNormX() {
        return (double)x/radius;
    }

    public double getNormY() {
        return (double)x/radius;
    }

    @Override
    public double getDistance() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    @Override
    public void draw(Graphics graphics, int color) {
        super.draw(graphics, color);
        graphics.drawCircle(getX()+x, getY()-y, radius/3, -1);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        super.draw(graphics, pixmap);
        graphics.drawCircle(getX()+x, getY()-y, 50, -1);
    }
}