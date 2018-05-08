package com.example.mfaella.physicsapp;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.MouseJoint;
import com.google.fpl.liquidfun.MouseJointDef;
import com.google.fpl.liquidfun.QueryCallback;


public class TouchConsumer {

    private final float offsetX, offsetY;
    private final float scaleX, scaleY;

    // keep track of what we are dragging
    private MouseJoint mouseJoint;
    private int activePointerID;
    private Fixture touchedFixture;

    private GameWorld gw;
    private QueryCallback touchQueryCallback = new TouchQueryCallback();

    // physical units, semi-side of a square around the touch point
    private final static float POINTER_SIZE = 0.5f;

    private class TouchQueryCallback extends QueryCallback
    {
        public boolean reportFixture(Fixture fixture) {
            touchedFixture = fixture;
            return true;
        }
    }

    /**
        scale{X,Y} are the scale factors from pixels to physics simulation coordinates
    */
    public TouchConsumer(GameWorld gw, float scaleX, float scaleY, float offsetX, float offsetY) {
        this.gw = gw;

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void consumeTouchEvent(Input.TouchEvent event)
    {
        switch (event.type) {
            case Input.TouchEvent.TOUCH_DOWN:
                consumeTouchDown(event);
                break;
            case Input.TouchEvent.TOUCH_UP:
                consumeTouchUp(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                consumeTouchMove(event);
                break;
        }
    }

    public void consumeTouchDown(Input.TouchEvent event) {
        int pointerId = event.pointer;

        // if we are already dragging with another finger, discard this event
        if (mouseJoint != null) return;

        float x = offsetX + event.x * scaleX,
              y = offsetY + event.y * scaleY;
        Log.d("MultiTouchHandler", "touch down at " + x + ", " + y);

        touchedFixture = null;
        gw.world.queryAABB(touchQueryCallback, x - POINTER_SIZE, y - POINTER_SIZE, x + POINTER_SIZE, y + POINTER_SIZE);
        if (touchedFixture != null) {
            MouseJointDef mouseJointDef = new MouseJointDef();
            Body touchedBody = touchedFixture.getBody();
            Object userData = touchedBody.getUserData();
            if (userData != null) {
                GameObject touchedGO = (GameObject) userData;
//                Log.d("MultiTouchHandler", "touched body " + touchedGO.name);
                mouseJointDef.setBodyA(touchedBody); // irrelevant but necessary
                mouseJointDef.setBodyB(touchedBody);
                mouseJointDef.setMaxForce(500 * touchedBody.getMass());
                mouseJointDef.setTarget(x, y);

                mouseJoint = gw.world.createMouseJoint(mouseJointDef);
                activePointerID = pointerId;
            }
        }
    }

    public void consumeTouchUp(Input.TouchEvent event) {
        if (mouseJoint != null && event.pointer == activePointerID) {
            Log.d("MultiTouchHandler", "Releasing joint");
            gw.world.destroyJoint(mouseJoint);
            mouseJoint = null;
            activePointerID = 0;
        }
    }

    public void consumeTouchMove(Input.TouchEvent event) {
        if (mouseJoint!=null && event.pointer == activePointerID) {
            float x = offsetX + event.x * scaleX,
                  y = offsetY + event.y * scaleY;
            Log.d("MultiTouchHandler", "active pointer moved to " + x + ", " + y);
            mouseJoint.setTarget(x, y);
        }
    }
}
