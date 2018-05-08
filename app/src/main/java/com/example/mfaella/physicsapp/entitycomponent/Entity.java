package com.example.mfaella.physicsapp.entitycomponent;

import java.util.EnumMap;
import java.util.Map;

public class Entity {
    private Map<Component.Type, Component> components = new EnumMap<Component.Type, Component>(Component.Type.class);

    public void addComponent(Component c) {
        c.setOwner(this);
        components.put(c.type(), c);
    }
    public Component getComponent(Component.Type type) {
        return components.get(type);
    }
}