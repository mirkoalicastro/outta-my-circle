package com.badlogic.androidgames.framework.impl;

import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidTile extends AndroidEffect {

    public AndroidTile(AndroidPixmap pixmap) {
        shader = new BitmapShader(pixmap.bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

}