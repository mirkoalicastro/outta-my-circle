package com.acg.outtamycircle.entitycomponent;

import android.graphics.Color;

import com.acg.outtamycircle.entitycomponent.impl.BoundsTest;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.physicsutilities.Converter;
import com.badlogic.androidgames.framework.Graphics;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.CircleDrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    private static World world;
    private static Graphics graphics;

    private EntityFactory(){}

    public static void setWorld(World world){ EntityFactory.world = world; }

    public static void setGraphics(Graphics graphics){ EntityFactory.graphics = graphics; }

    public static Arena createArena(int radius, int x, int y){
        Arena arena = new Arena();

        CircleDrawableComponent circleDrawableComponent = new CircleDrawableComponent(graphics, radius);
        circleDrawableComponent.setColor(Color.BLACK);
        circleDrawableComponent.setPosition(x, y);
        arena.addComponent(circleDrawableComponent);

        return arena;
    }

    public static GameCharacter createServerDefaultCharacter(int radius, int x, int y, int color){
        GameCharacter c = new GameCharacter();

        CircleDrawableComponent drawable = new CircleDrawableComponent(graphics, radius);
        drawable.setColor(color);
        drawable.setPosition(x, y);
        c.addComponent(drawable);

        c.addComponent(new DynamicCircle(world,
                Converter.frameToPhysicsRadius(radius),
                Converter.frameToPhysicsX(x),
                Converter.frameToPhysicsY(y)));

        return c;
    }

    //TODO privo di conversione
    public static GameCharacter createClientDefaultCharacter(int radius, int x, int y, int color){
        GameCharacter c = new GameCharacter();

        CircleDrawableComponent drawable = new CircleDrawableComponent(graphics, radius);
        drawable.setColor(color);
        drawable.setPosition(x, y);
        c.addComponent(drawable);

        return c;
    }
}
