package com.acg.outtamycircle.entitycomponent;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.CircleDrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.acg.outtamycircle.entitycomponent.impl.Character;
import com.google.fpl.liquidfun.World;

public class EntityFactory{
    private static World world;
    private static Graphics graphics;

    private EntityFactory(){}

    public static void setWorld(World world){ EntityFactory.world = world; }

    public static void setGraphics(Graphics graphics){ EntityFactory.graphics = graphics; }

    public static Character createDefaultCharacter(int radius, int x, int y, int color){
        Character c = new Character();

        c.addComponent(new PositionComponent(x, y));
        c.addComponent(new DynamicCircle(world, radius, x, y));

        CircleDrawableComponent drawable = new CircleDrawableComponent(graphics, radius);
        drawable.setColor(color);
        c.addComponent(drawable);

        return c;
    }

    public static Arena createArena(int radius, int x, int y){
        Arena arena = new Arena();

        CircleDrawableComponent circleDrawableComponent = new CircleDrawableComponent(graphics, radius);
        circleDrawableComponent.setColor(Color.CYAN);
        arena.addComponent(circleDrawableComponent);

        PositionComponent positionComponent = new PositionComponent(x,y);
        positionComponent.owner = arena; //madaffacca ma che lo fai a fare
        arena.addComponent(positionComponent);

        return arena;
    }
}
