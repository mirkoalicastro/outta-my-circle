package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

import java.util.LinkedList;
import java.util.List;

public class AndroidJoyStick extends AndroidCircularButton {
    private final Input input;
    private int x, y;
    private final List<Input.TouchEvent> buffer;
    private boolean haveTouched;

    public AndroidJoyStick(Input input, int x, int y, int radius) {
        super(x, y, radius);
        this.input = input;
        buffer = new LinkedList<>();
    }

    public List<Input.TouchEvent> processAndRelease() {
        List<Input.TouchEvent> all = input.getTouchEvents();
        for (Input.TouchEvent event : all) {
            boolean inBounds = inBounds(event);
            if(event.type == Input.TouchEvent.TOUCH_DOWN)
                haveTouched = inBounds;
            if (haveTouched && inBounds) {
                x = event.x - getX();
                y = getY() - event.y; //TODO why reversing is ok?
                buffer.add(event);
            }
        }
        all.removeAll(buffer);
        buffer.clear();
        return all;
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(y,x));
    }

    public double getDistance() {
        //TODO maybe it could be better just the sum of the absolute values?
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    public void draw(Graphics graphics, int color) {
        super.draw(graphics, color);
        graphics.drawCircle(getX()+x, getY()-y, 10, -1);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        super.draw(graphics, pixmap);
    }
}