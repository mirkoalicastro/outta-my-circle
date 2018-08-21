package com.badlogic.androidgames.framework;

import java.util.List;

public interface Joystick extends Button {
    List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events);
    double getAngle();
    double getDistance();
    double getNormX();
    double getNormY();
}