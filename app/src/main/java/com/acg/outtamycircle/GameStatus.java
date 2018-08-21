package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.impl.GameObject;

import java.util.LinkedList;
import java.util.List;

public class GameStatus {
    private final List<GameObject> gameObjectList;
    {
        gameObjectList = new LinkedList<>();
    }
    public synchronized void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
    }
}