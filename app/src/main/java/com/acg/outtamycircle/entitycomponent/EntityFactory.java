package com.acg.outtamycircle.entitycomponent;

import android.graphics.Color;

import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
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
        arena.addComponent(circleDrawableComponent);

        PositionComponent positionComponent = new PositionComponent(x,y);
        positionComponent.owner = arena;
        arena.addComponent(positionComponent);

        return arena;
    }

    public static GameCharacter createServerDefaultCharacter(int radius, int x, int y, int color){
        GameCharacter c = new GameCharacter();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new DynamicCircle(world, radius, x, y));

        CircleDrawableComponent drawable = new CircleDrawableComponent(graphics, radius);
        drawable.setColor(color);
        c.addComponent(drawable);

        return c;
    }

    public static GameCharacter createClientDefaultCharacter(int radius, int x, int y, int color){
        GameCharacter c = new GameCharacter();

        c.addComponent(new PositionComponent(x, y));

        CircleDrawableComponent drawable = new CircleDrawableComponent(graphics, radius);
        drawable.setColor(color);
        c.addComponent(drawable);

        return c;
    }
}
