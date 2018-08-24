package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.GameObject;
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
}