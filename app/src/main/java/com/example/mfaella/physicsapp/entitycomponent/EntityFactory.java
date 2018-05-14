package com.example.mfaella.physicsapp.entitycomponent;

import com.example.mfaella.physicsapp.PowerUp;
import com.example.mfaella.physicsapp.Character;

public class EntityFactory {
    public Entity makePowerUp(){
        PowerUp e = null;
        //TODO distruggi tutto
        //serie di addComponent()
        return e;
    }

    public Entity makeCharacter(){
        Character e = new Character();
        //serie di addComponent()
        return e;
    }
}
