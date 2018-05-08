package com.example.mfaella.physicsapp;

import android.graphics.Canvas;

import com.example.mfaella.physicsapp.entitycomponent.Component;
import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

class BoxDrawableComponent extends DrawableComponent {
    @Override
    public void draw(Canvas canvas) {
        PositionComponent pos = (PositionComponent)
                owner.getComponent(Component.Type.Position);
        float x = pos.x;

        //TODO draaaaaaw
        //canvas.drawRect(...)
    }
}