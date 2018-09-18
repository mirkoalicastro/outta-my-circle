package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.GameStatus;

public abstract class Powerup extends GameObject{
    protected final GameStatus status;
    protected GameCharacter character;
    protected boolean assigned;

    public boolean getAssigned() {
        return assigned;
    }

    public Powerup setAssigned(boolean assigned) {
        this.assigned = assigned;
        return this;
    }

    public Powerup(GameStatus status, int id){
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
