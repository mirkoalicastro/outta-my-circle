package com.acg.outtamycircle;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class OuttaMyCircleGame extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
