package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Color;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.example.mfaella.physicsapp.entitycomponent.impl.Arena;
import com.example.mfaella.physicsapp.entitycomponent.impl.CircleDrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.CirclePhysicsComponent;
import com.example.mfaella.physicsapp.entitycomponent.impl.PowerUp;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    private static World world;
    private static Graphics graphics;

    private EntityFactory(){}

    public static void setWorld(World world){ EntityFactory.world = world; }

    public static void setGraphics(Graphics graphics){ EntityFactory.graphics = graphics; }

    public static Character createDefaultCharacter(int radius, int x, int y){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new CirclePhysicsComponent(world, radius));

        c.addComponent(new CircleDrawableComponent(graphics, radius));

        return c;
    }

    public static Arena createArena(int radius, int x, int y){
        Arena arena = new Arena();

        CircleDrawableComponent circleDrawableComponent = new CircleDrawableComponent(graphics, radius);
        circleDrawableComponent.setColor(Color.CYAN);
        arena.addComponent(circleDrawableComponent);

        PositionComponent positionComponent = new PositionComponent(x,y);
        positionComponent.owner = arena;
        arena.addComponent(positionComponent);

        return arena;
    }
}
