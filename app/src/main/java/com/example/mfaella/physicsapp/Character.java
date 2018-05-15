package com.example.mfaella.physicsapp;

import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;
import com.google.fpl.liquidfun.World;

public class Character extends GameObject{
    PowerUp powerUp = null;

    public static Character defaultFactory(World world, float radius, float x, float y){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new CirclePhysicsComponent(world, radius));
        c.addComponent(new CircleDrawableComponent());

        return c;
    }
}