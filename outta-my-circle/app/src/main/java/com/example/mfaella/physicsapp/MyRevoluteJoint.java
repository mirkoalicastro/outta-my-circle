package com.example.mfaella.physicsapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.Vec2;

/**
 *
 * Created by mfaella on 27/02/16.
 */
public class MyRevoluteJoint
{
    Joint joint;

    public MyRevoluteJoint(GameWorld gw, Body a, Body b)
    {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(-1f, -1f);
        jointDef.setLocalAnchorB(-1f, -1f);
        // add friction
        jointDef.setEnableMotor(true);
        jointDef.setMotorSpeed(0);
        jointDef.setMaxMotorTorque(8f);
        joint = gw.world.createJoint(jointDef);

        jointDef.delete();
    }
}
