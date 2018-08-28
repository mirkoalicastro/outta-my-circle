package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.google.fpl.liquidfun.World;

import java.util.LinkedList;
import java.util.List;

public class GameStatus {
    private final List<GameObject> gameObjectList = new LinkedList<>();

    Arena arena;
    GameCharacter[] characters; //main character at pos 0

    void setArena(Arena arena){
        this.arena = arena;
    }

    void setCharacters(GameCharacter[] characters) {
        this.characters = characters;
    }

    //TODO serve synch?
    public synchronized void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
    }

    public boolean collide(GameCharacter ch1, GameCharacter ch2){
        DynamicCircle circle1 = (DynamicCircle)ch1.getComponent(Component.Type.Physics);
        DynamicCircle circle2 = (DynamicCircle)ch2.getComponent(Component.Type.Physics);

        return Math.pow(circle1.getX()-circle2.getX(), 2)
                + Math.pow(circle1.getY()-circle2.getY(), 2)
                < Math.pow(circle1.radius+circle2.radius, 2);
    }
}