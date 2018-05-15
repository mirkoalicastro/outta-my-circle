package com.example.mfaella.physicsapp.entitycomponent;

import com.example.mfaella.physicsapp.entitycomponent.impl.CircleDrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.CirclePhysicsComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.PowerUp;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    public static Entity createPowerUp(){
        PowerUp e = null;
        //TODO distruggi tutto
        //serie di addComponent()
        return e;
    }

    public static Character createDefaultCharacter(World world, float radius, float x, float y){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new CirclePhysicsComponent(world, radius));
        c.addComponent(new CircleDrawableComponent());

        return c;
    }

    public static Entity createCharacter(){
        Character e = new Character();
        //serie di addComponent()
        return e;
    }
}
