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
    private int x, y;
    private final List<Input.TouchEvent> buffer;
    private final int radius;
    private int pointer = -1;
    private static final int DEFAULT_PRIMARY_COLOR = Color.DKGRAY;
    private static final int DEFAULT_SECONDARY_COLOR = Color.WHITE;

    public AndroidJoystick(Input input, Graphics graphics, int x, int y, int radius) {
        super(graphics, x, y, radius);
        this.radius = radius;
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
        return Math.toDegrees(Math.atan2(y,x));
    }

    public double getNormX() {
        return (double)x/radius;
    }

    public double getNormY() {
        return (double)-y/radius;
    } //TODO asse invertito

    @Override
    public double getDistance() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    @Override
    public void draw(int color) {
        draw(color, DEFAULT_SECONDARY_COLOR);
    }

    public void draw(int color1, int color2) {
        super.draw(color1);
        graphics.drawCircle(getX()+x, getY()-y, radius/2, color2);
    }

    public void draw(int color1, int color2, int strokeWidth, int colorStroke) {
        draw(color1,color2);
        super.drawStroke(strokeWidth,colorStroke);
    }

    public void draw(int color1, int color2, Pixmap pixmap, int strokeWidth, int colorStroke) {
        draw(color1, color2, pixmap);
        super.drawStroke(strokeWidth, colorStroke);
    }

    @Override
    public void draw(Pixmap pixmap) {
        draw(DEFAULT_PRIMARY_COLOR, DEFAULT_SECONDARY_COLOR, pixmap);
    }

    public void draw(int color1, int color2, Pixmap pixmap) {
        draw(color1, color2);
        super.draw(pixmap);
    }

}