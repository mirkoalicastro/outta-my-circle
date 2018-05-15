package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Paint;

import com.example.mfaella.physicsapp.entitycomponent.impl.CircleDrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.CirclePhysicsComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.PowerUp;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    public static Entity createPowerUp(){
        PowerUp e = null;
        //serie di addComponent()
        return e;
    }

    public static Character createDefaultCharacter(World world, float radius, float x, float y){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new CirclePhysicsComponent(world, radius));

        Paint paint = new Paint();
        paint.setARGB(255,255,0,0);

        c.addComponent(new CircleDrawableComponent(paint));

        return c;
    }

    public static Entity createCharacter(){
        Character e = new Character();
        //serie di addComponent()
        return e;
    }
}
