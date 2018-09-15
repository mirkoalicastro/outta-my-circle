package com.acg.outtamycircle.entitycomponent;

import java.util.EnumMap;
import java.util.Map;

public class Entity {
    private final Map<Component.Type, Component> components;

    public Entity() {
        components = new EnumMap<>(Component.Type.class);
    }

    public void addComponent(Component c) {
        components.put(c.type(), c);
    }
    public Component getComponent(Component.Type type) {
        return components.get(type);
    }
    public void removeComponent(Component.Type type){
        components.remove(type);
    }
}