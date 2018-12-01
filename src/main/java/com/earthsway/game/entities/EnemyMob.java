package com.earthsway.game.entities;

import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.utilities.Coords;
import com.earthsway.game.utilities.EntityType;
import com.earthsway.game.utilities.Health;
import com.earthsway.game.utilities.Shield;

import java.awt.*;

public abstract class EnemyMob extends Mob {
    public EnemyMob(Level level, String name, int x, int y, int[] collisionBox, Coords respawnCoords, int speed, boolean canMoveDiagonal,
                    int scale, Health health, Shield shield, boolean respawnWithShield, boolean damageable, double hitCooldown, boolean canSwim, EntityType entityType,
                    boolean canMove, boolean damaging) {
        super(level, name, x, y, collisionBox, respawnCoords, speed, canMoveDiagonal, scale, health, shield, respawnWithShield, damageable, hitCooldown, canSwim, entityType);
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public void render(Graphics g) {

    }
}
