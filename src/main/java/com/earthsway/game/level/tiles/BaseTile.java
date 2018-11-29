package com.earthsway.game.level.tiles;

import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.utilities.Biome;
import com.earthsway.game.utilities.SoundType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BaseTile extends Tile{

    protected int tileId;
    protected int tileColor;

    public BaseTile(int id, int x, int y, int tileColor, BufferedImage sprite) {
        super(id, false, false, false, false, -0, sprite, null, null);
        this.tileId = x + y * 32;
        this.tileColor = tileColor;
    }

    public BaseTile(int id, int x, int y, int tileColor, BufferedImage sprite, SoundType soundType) {
        super(id, false, false, false, false, -0, sprite, soundType, null);
        this.tileId = x + y * 32;
        this.tileColor = tileColor;
    }

    public BaseTile(int id, int x, int y, int tileColor, BufferedImage sprite, Biome biome) {
        super(id, false, false, false, false, -0, sprite, null, biome);
        this.tileId = x + y * 32;
        this.tileColor = tileColor;
    }

    public BaseTile(int id, int x, int y, int tileColor, BufferedImage sprite, SoundType soundType, Biome biome) {
        super(id, false, false, false, false, -0, sprite, soundType, biome);
        this.tileId = x + y * 32;
        this.tileColor = tileColor;
    }

    public void tick(){}

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, tileColor);
    }
}
