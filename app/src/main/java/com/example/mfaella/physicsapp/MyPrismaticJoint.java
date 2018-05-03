package com.example.mfaella.physicsapp;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.RevoluteJointDef;

/**
 *
 * Created by mfaella on 27/02/16.
 */
public class MyPrismaticJoint
{
    Joint joint;

    public MyPrismaticJoint(GameWorld gw, Body a, Body b)
    {
        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(-1f, -1f);
        jointDef.setLocalAnchorB(-1f, -1f);
        jointDef.setLocalAxisA(1f,1f);
        // add friction
        jointDef.setEnableMotor(true);
        jointDef.setMotorSpeed(-2f);
        jointDef.setMaxMotorForce(10f);
        joint = gw.world.createJoint(jointDef);

        jointDef.delete();
    }
}
