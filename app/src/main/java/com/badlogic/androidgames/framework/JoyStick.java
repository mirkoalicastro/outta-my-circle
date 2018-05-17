package com.badlogic.androidgames.framework;

import java.util.List;

public interface JoyStick extends Button {
    List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events);
    List<Input.TouchEvent> processAndRelease();
    double getAngle();
    double getDistance();
}