package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.acg.outtamycircle.utilities.MyList;

public class GameStatus {
    //TODO private final a TUTTO!?!?!?
    Entity arena;

    GameCharacter playerOne;
    GameCharacter[] characters;

    final MyList<GameCharacter> living = new MyList<>();
    final MyList<GameCharacter> dying = new MyList<>();

    final MyList<Powerup> actives = new MyList<>();
    final MyList<Powerup> inactives = new MyList<>();
    private int activePowerupsNumber;

    void setArena(Entity arena){
        this.arena = arena;
    }

    public void setCharacters(GameCharacter... characters){
        for(int i=0 ; i<characters.length ; i++)
            living.add(characters[i]);
        this.characters = characters;
    }

    public void setPlayerOne(GameCharacter ch){
        playerOne = ch;
    }

    public MyList<GameCharacter> getLiving(){
        return living;
    }

    public int getActivePowerupsNumber(){ return activePowerupsNumber; }

    public MyList<Powerup> getActivePowerups() {
        return actives;
    }

    public MyList<Powerup> getInactivePowerups() {
        return inactives;
    }
}