package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.Powerup;
import com.acg.outtamycircle.utilities.MyList;

import java.util.LinkedList;
import java.util.List;

public class GameStatus {
    //TODO private final
    Arena arena;
    Powerup powerup;

    GameCharacter playerOne;
    GameCharacter[] characters;

    final MyList<GameCharacter> living = new MyList<>();
    final MyList<GameCharacter> dying = new MyList<>();

    void setArena(Arena arena){
        this.arena = arena;
    }

    public void add(GameCharacter gc){ living.add(gc); }

    public void setCharacters(GameCharacter ... characters){
        for(int i=0 ; i<characters.length ; i++)
            living.add(characters[i]);
        this.characters = characters;
    }

    public void setPowerup(Powerup pu){ powerup = pu; }

    public void setPlayerOne(GameCharacter ch){
        playerOne = ch;
    }
}