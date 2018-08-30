package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoystick extends AndroidCircularButton implements Joystick {
    private int x, y;
    private final List<Input.TouchEvent> buffer = new LinkedList<>();
    private final int radius;
    private final Effect effect;
    private int pointer = -1;

    private static final int DEFAULT_PRIMARY_COLOR = Color.DKGRAY;
    private static final int DEFAULT_SECONDARY_COLOR = Color.WHITE;

    public AndroidJoystick(Graphics graphics, int x, int y, int radius, Effect effect) {
        super(graphics, x, y, radius);
        this.radius = radius;
        this.effect = effect;
    }
    public AndroidJoystick(Graphics graphics, int x, int y, int radius) {
        this(graphics,x,y,radius,null);
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
        drawEffect();
        graphics.drawCircle(getX()+x, getY()-y, radius/2, color2);
    }

    private void drawEffect() {
        if(effect != null)
            graphics.drawEffect(effect,super.getX(), super.getY(), radius*2, radius*2);
    }

    public void draw(int color1, int color2, int strokeWidth, int colorStroke) {
        super.drawStroke(strokeWidth,colorStroke);
        draw(color1,color2);
    }

    public void draw(int color1, int color2, Pixmap pixmap, int strokeWidth, int colorStroke) {
        super.drawStroke(strokeWidth, colorStroke);
        draw(color1, color2, pixmap);
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