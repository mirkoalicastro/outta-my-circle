package com.badlogic.androidgames.framework;

import java.util.List;

public interface JoyStick extends Button {
    List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events);
    double getAngle();
    double getDistance();
}