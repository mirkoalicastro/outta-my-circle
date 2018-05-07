package com.example.mfaella.physicsapp;

import android.graphics.Canvas;

import com.example.mfaella.physicsapp.entitycomponent.ComponentType;
import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

class BoxDrawableComponent extends DrawableComponent {
    @Override
    public void draw(Canvas canvas) {
        PositionComponent pos = (PositionComponent)
                owner.getComponent(ComponentType.Position);
        float x = pos.x;

        //TODO draaaaaaw
        //canvas.drawRect(...)
    }
}