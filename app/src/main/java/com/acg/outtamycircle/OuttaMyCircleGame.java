package com.acg.outtamycircle;

import com.acg.outtamycircle.network.googleimpl.GoogleAndroidGame;
import com.badlogic.androidgames.framework.Screen;

public class OuttaMyCircleGame extends GoogleAndroidGame {

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
