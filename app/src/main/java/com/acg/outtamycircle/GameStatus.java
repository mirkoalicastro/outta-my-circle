package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
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

    //TODO lista di powerup e override di equals per objectId
    Powerup powerup;
    final MyList<Powerup> actives = new MyList<>();

    final MyList<AttackComponent> activeAttacks = new MyList<>();


    void setArena(Entity arena){
        this.arena = arena;
    }

    public void setCharacters(GameCharacter... characters){
        for (GameCharacter character : characters)
            living.add(character);
        this.characters = characters;
    }

    public void setPlayerOne(GameCharacter ch){
        playerOne = ch;
    }

    public MyList<GameCharacter> getLiving(){
        return living;
    }

    public MyList<Powerup> getActivePowerups() {
        return actives;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    public GameCharacter[] getCharacters() {
        return characters;
    }

    public void setPowerup(Powerup powerup) {
        this.powerup = powerup;
    }
}