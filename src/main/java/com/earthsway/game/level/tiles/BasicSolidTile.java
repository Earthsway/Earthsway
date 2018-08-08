package com.earthsway.game.level.tiles;

import com.earthsway.game.utilities.Biome;
import com.earthsway.game.utilities.SoundType;

public class BasicSolidTile extends BaseTile {
    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, x, y, tileColor, levelColor);
        this.solid = true;
    }
    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor, SoundType soundType) {
        super(id, x, y, tileColor, levelColor, soundType);
        this.solid = true;
    }
    public BasicSolidTile(int id, int x, int y, int tileColor, int levelColor, Biome biome) {
        super(id, x, y, tileColor, levelColor, null, biome);
        this.solid = true;
    }
}
