package com.earthsway.game.level.tiles;

public class BasicSolidDamagingTile extends BaseTile {
    public BasicSolidDamagingTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, x, y, tileColor, levelColor);
        this.damaging = true;
        this.solid = true;
    }
}
