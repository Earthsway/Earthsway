package com.earthsway.game.level.tiles;

public class BasicDamagingTile extends BaseTile {
    public BasicDamagingTile(int id, int x, int y, int tileColor, int levelColor, int damageAmount, boolean constantDamaging) {
        super(id, x, y, tileColor, levelColor);
        this.damaging = true;
        this.constantDamaging = constantDamaging;
        this.damageAmount = damageAmount;
    }
}
