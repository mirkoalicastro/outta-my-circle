package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.GameStatus;

public abstract class Powerup extends GameObject{
    protected final GameStatus status;
    protected GameCharacter character;

    public Powerup(GameStatus status, short id){
        super(id);
        this.status = status;
    }

    /*@Override
    public Type type() {
        return Type.Powerup;
    }*/

    public abstract void start();

    public abstract void stop();

    public abstract boolean isEnded();

    public Powerup setGameCharacter(){
        this.character = character;
        return this;
    }
}
