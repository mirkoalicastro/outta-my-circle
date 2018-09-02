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
    Arena arena;
    GameCharacter[] characters;
    boolean[] alives;

    void setArena(Arena arena){
        this.arena = arena;
    }

    void setCharacters(GameCharacter[] characters) {
        this.characters = characters;
        alives = new boolean[characters.length];
        for(int i=0; i<alives.length ; i++)
            alives[i] = true;
    }
}