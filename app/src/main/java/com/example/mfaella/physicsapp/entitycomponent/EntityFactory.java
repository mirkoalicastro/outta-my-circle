package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Paint;

import com.example.mfaella.physicsapp.entitycomponent.impl.Arena;
import com.example.mfaella.physicsapp.entitycomponent.impl.CircleDrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.CirclePhysicsComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.PowerUp;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    public static Character createDefaultCharacter(World world, float radius, float x, float y){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new CirclePhysicsComponent(world, radius));

        Paint paint = new Paint();
        paint.setARGB(255,255,0,0);

        c.addComponent(new CircleDrawableComponent(paint, radius));

        return c;
    }

    public static Arena createArena(long radius){
        Arena a = new Arena();

        Paint paint = new Paint();
        paint.setARGB(255,132,112,255); //light blue

        a.addComponent(new CircleDrawableComponent(paint, radius));

        return a;
    }
}
