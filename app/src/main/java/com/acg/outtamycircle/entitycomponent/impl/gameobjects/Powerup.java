package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.GameStatus;

public abstract class Powerup extends GameObject{
    protected final GameStatus status;
    protected GameCharacter character;

    public Powerup(GameStatus status, short id){
        super(id, Type.POWERUP);
        this.status = status;
    }

    public abstract byte getCode();

    public abstract void start();

    public abstract void work();

    public abstract void stop();

    public abstract boolean isEnded();

    public Powerup setGameCharacter(GameCharacter character){
        this.character = character;
        return this;
    }

    public GameCharacter getCharacter(){
        return character;
    }
}
