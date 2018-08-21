package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoystick extends AndroidCircularButton implements Joystick {
    private final Input input;
    private int x, y;
    private final List<Input.TouchEvent> buffer;
    private final int radius;
    private int pointer = -1;

    public AndroidJoystick(Input input, int x, int y, int radius) {
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
                buffer.add(event);
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
        if(events.size() > 0) {
            Log.d("LAST", "sono " + pointer  + "primo" + events.get(0).pointer);
            Log.d("LAST", "sono " + pointer + "ultimo" + events.get(events.size() - 1).pointer);
        }
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
        return (double)y/radius;
    }

    @Override
    public double getDistance() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    @Override
    public void draw(Graphics graphics, int color) {
        draw(graphics, color, -1);
    }

    public void draw(Graphics graphics, int color1, int color2) {
        super.draw(graphics, color1);
        graphics.drawCircle(getX()+x, getY()-y, radius/2, color2);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        draw(graphics, Color.DKGRAY, -1, pixmap);
    }

    public void draw(Graphics graphics, int color1, int color2, Pixmap pixmap) {
        draw(graphics, color1, color2);
        super.draw(graphics, pixmap);
    }

}