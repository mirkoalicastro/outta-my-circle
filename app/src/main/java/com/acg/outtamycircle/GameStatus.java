package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.impl.GameObject;

import java.util.LinkedList;
import java.util.List;

public class GameStatus {
    private static final List<GameObject> gameObjectList;
    static {
        gameObjectList = new LinkedList<>();
    }
    public static synchronized void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
    }
}
