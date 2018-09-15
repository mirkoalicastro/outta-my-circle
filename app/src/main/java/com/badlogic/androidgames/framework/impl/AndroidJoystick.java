package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoystick extends AndroidCircularButton implements Joystick {
    private int xPad, yPad;
    private final List<Input.TouchEvent> buffer = new LinkedList<>();
    private int pointer = -1;
    protected Integer secondaryColor;

    public AndroidJoystick(Graphics graphics, int x, int y, int radius) {
        super(graphics, x, y, radius);
    }

    @Override
    public List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events) {
        if(!isEnabled())
            return events;
        for (Input.TouchEvent event : events) {
            if(event.type == Input.TouchEvent.TOUCH_DOWN)
                if(Math.pow(event.x-x,2)+Math.pow(event.y-y,2)<Math.pow(radius*2,2))
                    pointer = event.pointer;
            if(event.type == Input.TouchEvent.TOUCH_UP && pointer == event.pointer) {
                pointer = -1;
                xPad = yPad = 0;
                buffer.add(event);
            }
            if (event.pointer == pointer) {
                xPad = event.x - x;
                yPad = event.y - y;
                int tmpx = Math.abs((int)(radius*Math.cos(Math.atan2(yPad, xPad))));
                int tmpy = Math.abs((int)(radius*Math.sin(Math.atan2(yPad, xPad))));
                xPad = Math.max(Math.min(tmpx, xPad), tmpx*-1);
                yPad = Math.max(Math.min(tmpy, yPad), tmpy*-1);
                buffer.add(event);
            }
        }
        events.removeAll(buffer);
        buffer.clear();
        return events;
    }

    @Override
    public float getAngle() {
        return (float) Math.toDegrees(Math.atan2(yPad, xPad));
    }

    public float getNormX() {
        return (float) xPad / radius;
    }

    public float getNormY() {
        return (float) yPad / radius;
    }

    @Override
    public float getDistance() {
        return (float) Math.sqrt(Math.pow(xPad,2) + Math.pow(yPad,2));
    }

    @Override
    public void draw() {
        super.draw();
        if(secondaryColor != null)
            graphics.drawCircle(x + xPad, y + yPad, radius/2, secondaryColor);
    }

    public AndroidJoystick setSecondaryColor(Integer secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }

    public Integer getSecondaryColor() {
        return secondaryColor;
    }

}