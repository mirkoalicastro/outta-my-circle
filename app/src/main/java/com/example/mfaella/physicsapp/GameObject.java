package com.example.mfaella.physicsapp;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.fpl.liquidfun.Body;

/**
 * Created by mfaella on 27/02/16.
 */
public abstract class GameObject {
    protected Body body;
    protected String name;
    protected GameWorld gw;

    public GameObject(GameWorld gw)
    {
        this.gw = gw;
    }
    public boolean draw(Bitmap buffer)
    {
        if (body!=null) {
            float x = body.getPositionX(),
                  y = body.getPositionY(),
                  angle = body.getAngle();
            // Log.d("GameObject", "x=" + x + "\t y=" + y);
            if (x > gw.physicalSize.xmin && x < gw.physicalSize.xmax &&
                y > gw.physicalSize.ymin && y < gw.physicalSize.ymax) {
                float screen_x = gw.toPixelsX(x),
                        screen_y = gw.toPixelsY(y);
                this.draw(buffer, screen_x, screen_y, angle);
                return true;
            } else
                return false;
        } else {
            this.draw(buffer, 0, 0, 0);
            return true;
        }
    }

    public abstract void draw(Bitmap buf, float x, float y, float angle);
}
