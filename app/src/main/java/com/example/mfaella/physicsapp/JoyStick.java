package com.example.mfaella.physicsapp;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;

import java.util.LinkedList;
import java.util.List;

public class JoyStick extends AndroidCircularButton {
    private final Input input;
    private final Graphics graphics;
    private int x = 0, y = 0;
    private final List<Input.TouchEvent> buffer;
    private boolean haveTouched = false;

    public JoyStick(Input input, Graphics graphics, int x, int y, int radius) {
        super(x, y, radius);
        this.input = input;
        this.graphics = graphics;
        buffer = new LinkedList<>();
    }

    public List<Input.TouchEvent> processAndRelease() {
        List<Input.TouchEvent> all = input.getTouchEvents();
        buffer.clear();
        for (Input.TouchEvent event : all) {
            boolean inBounds = inBounds(event);
            if(event.type == Input.TouchEvent.TOUCH_DOWN) {
                haveTouched = inBounds;
            }
            int centerX = getX();
            int centerY = getY();
            if (haveTouched && inBounds) {
                x = event.x - centerX;
                y = event.y - centerY;
                buffer.add(event);
            }
        }
        all.removeAll(buffer);
        return all;
    }

    public double getAngle() {
        if(x == 0 && y == 0)
            return 0;
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    public void draw(Graphics graphics, int color) {
        super.draw(graphics, color);
        graphics.drawCircle(getX()+x, getY()+y, 50, Color.WHITE);
    }

    @Override
    public void draw(Graphics graphics, Pixmap pixmap) {
        super.draw(graphics, pixmap);
    }
}