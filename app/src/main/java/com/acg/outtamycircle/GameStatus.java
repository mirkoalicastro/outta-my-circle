package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Arena;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.utilities.MyList;

public class GameStatus {
    //TODO private final
    Arena arena;

    GameCharacter playerOne;
    GameCharacter[] characters;

    final MyList<GameCharacter> living = new MyList<>();
    final MyList<GameCharacter> dying = new MyList<>();

    final MyList<GameCharacter> powerups = new MyList<>();
    private int activePowerupsNumber;

    void setArena(Arena arena){
        this.arena = arena;
    }

    public void add(GameCharacter gc){ living.add(gc); }

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

    public MyList<GameCharacter> getPowerups(){
        return powerups;
    }

    public int getActivePowerupsNumber(){ return activePowerupsNumber; }
}