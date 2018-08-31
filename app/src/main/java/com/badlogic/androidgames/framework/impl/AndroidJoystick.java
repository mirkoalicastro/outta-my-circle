package com.badlogic.androidgames.framework.impl;

import android.util.Log;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoystick extends AndroidCircularButton implements Joystick {
    private int xPad, yPad;
    private final List<Input.TouchEvent> buffer = new LinkedList<>();
    private final int radius;
    private final Effect effect;
    private int pointer = -1;
    protected Integer secondaryColor;

    public AndroidJoystick(Graphics graphics, Effect effect, int xPad, int yPad, int radius, Integer primaryColor, Integer secondaryColor, Pixmap pixmap, int strokeWidth, Integer strokeColor) {
        super(graphics, effect, xPad, yPad, radius, primaryColor, pixmap, strokeWidth, strokeColor);
        this.secondaryColor = secondaryColor;
        this.radius = radius;
        this.effect = effect; //TODO can be null
    }

    @Override
    public List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events) {
        for (Input.TouchEvent event : events) {
            if(event.type == Input.TouchEvent.TOUCH_DOWN)
                if(Math.pow(event.x-x,2)+Math.pow(event.y-y,2)<Math.pow(radius*2,2))
                    pointer = event.pointer;
            if(event.type == Input.TouchEvent.TOUCH_UP && pointer == event.pointer) {
                pointer = -1;
                xPad = 0;
                yPad = 0;
                buffer.add(event);
            }
            if (event.pointer == pointer) {
                xPad = event.x - x;
                yPad = y - event.y;
                int tmpx = Math.abs((int)((radius)*Math.cos(Math.atan2(yPad, xPad))));
                int tmpy = Math.abs((int)((radius)*Math.sin(Math.atan2(yPad, xPad))));
                xPad = Math.max(Math.min(tmpx, xPad), tmpx*-1);
                yPad = Math.max(Math.min(tmpy, yPad), tmpy*-1);
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
        return Math.toDegrees(Math.atan2(yPad, xPad));
    }

    public double getNormX() {
        return (double) xPad /radius;
    }

    public double getNormY() {
        return (double)-yPad /radius;
    } //TODO asse invertito

    @Override
    public double getDistance() {
        return Math.sqrt(Math.pow(xPad,2) + Math.pow(yPad,2));
    }

    @Override
    public void draw() {
        super.draw();
        graphics.drawCircle(x + xPad, y - yPad, radius/2, secondaryColor);
    }

}