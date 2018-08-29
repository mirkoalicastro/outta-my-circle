package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.androidgames.framework.impl.AndroidGame;

public abstract class GoogleAndroidGame extends AndroidGame {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleManager.init(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleManager.handleResult(requestCode, resultCode, data);
    }
}
