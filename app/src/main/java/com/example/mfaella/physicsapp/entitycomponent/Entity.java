package com.example.mfaella.physicsapp.entitycomponent;

import java.util.EnumMap;
import java.util.Map;

public class Entity {
    private Map<ComponentType, Component> components = new EnumMap<ComponentType, Component>(ComponentType.class);

    public void addComponent(Component c) {
        c.setOwner(this);
        components.put(c.type(), c);
    }
    public Component getComponent(ComponentType type) {
        return components.get(type);
    }
}