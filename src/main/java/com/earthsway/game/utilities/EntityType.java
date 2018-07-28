package com.earthsway.game.utilities;

import com.earthsway.game.entities.Entity;
import com.earthsway.game.entities.Player;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.entities.Worker;

public enum EntityType {
    PLAYER("player", Player.class, 0),
    PLAYER_MULTIPLAYER("player_multiplayer", PlayerMP.class, 1),
    WORKER("worker", Worker.class, 2),
    UNKNOWN(null, null, -1);

    private String name;
    private Class<? extends Entity> clazz;
    private short typeId;

    private EntityType(String name, Class<? extends Entity> clazz, int typeId) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
    }
    public String getName() {
        return this.name;
    }
    public Class<? extends Entity> getEntityClass() {
        return this.clazz;
    }
    public short getTypeId() {return this.typeId;}
}
