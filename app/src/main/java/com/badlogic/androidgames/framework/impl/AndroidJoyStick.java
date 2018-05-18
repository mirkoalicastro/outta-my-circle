package com.badlogic.androidgames.framework.impl;

import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.JoyStick;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

//TODO should it automatically go back to 0,0?
public class AndroidJoyStick extends AndroidCircularButton implements JoyStick {
    private final Input input;
    private int x, y;
    private final List<Input.TouchEvent> buffer;

    public AndroidJoyStick(Input input, int x, int y, int radius) {
        super(x, y, radius);
        this.input = input;
        buffer = new LinkedList<>();
    }

    @Override
    public List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events) {
        for (Input.TouchEvent event : events) {
            if (inBounds(event)) {
                x = event.x - getX();
                y = getY() - event.y; //TODO why reversing is ok?
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
    public List<Input.TouchEvent> processAndRelease() {
        return processAndRelease(input.getTouchEvents());
    }

    @Override
    public double getAngle() {
        return Math.toDegrees(Math.atan2(y,x));
    }

    @Override
    public double getDistance() {
        //TODO maybe it could be better just the sum of the absolute values?
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    @Override
    public void draw(Graphics graphics, int color) {
        super.draw(graphics, color);
        graphics.drawCircle(getX()+x, getY()-y, 50, -1);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        super.draw(graphics, pixmap);
        graphics.drawCircle(getX()+x, getY()-y, 50, -1);
    }
}