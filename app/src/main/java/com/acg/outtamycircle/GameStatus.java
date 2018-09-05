package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.Powerup;
import com.google.fpl.liquidfun.World;

import java.util.LinkedList;
import java.util.List;

public class GameStatus {
    Arena arena; //TODO private final
    GameCharacter[] characters; //old
    boolean[] alives;
    Powerup powerup;

    private final List<GameCharacter> living = new LinkedList<>();
    //private final List<GameCharacter> dead = new LinkedList<>();

    //boolean[] inGame;



    void setArena(Arena arena){
        this.arena = arena;
    }

    public void add(GameCharacter gc){ living.add(gc); }

    /*public getGameCharacter(){
        living

    }*/

    /*old*/
    public void setCharacters(GameCharacter[] gc){
        characters = gc;
        alives = new boolean[characters.length];
        for(int i=0 ; i<characters.length; i++)
            alives[i] = true;
    }

    public void setPowerup(Powerup pu){ powerup = pu; }
}