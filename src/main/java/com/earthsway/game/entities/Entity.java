package com.earthsway.game.entities;

import com.earthsway.game.utilities.EntityType;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;

import java.awt.*;

public abstract class Entity {

    public int x,y;
    protected Level level;
    protected EntityType entityType;

    public Entity(Level level){
        init(level);
    }

    public final void init(Level level){
        this.level = level;
    }

    public abstract void tick();
    public abstract void render();

    public EntityType getEntityType() {return entityType;}
}
