package com.earthsway.game.level.tiles;

import com.earthsway.game.gfx.Assets;
import com.earthsway.game.utilities.Biome;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.level.Level;
import com.earthsway.game.utilities.SoundType;

import javax.swing.*;
import java.awt.image.BufferedImage;

public abstract class Tile {
    public static Tile[] tiles = new Tile[256];

    public static final Tile VOID = new BasicSolidTile(0,0,0, Assets.voidTile, Biome.INVALID);
    public static final Tile STONE = new BaseTile(1, 1,0, Colors.get(-1, 333, -1, -1), 0xFF555555, SoundType.CAVE, Biome.CAVE);
    public static final Tile GRASS = new BaseTile(2, 2,0, Colors.get(-1, 131, 141, -1), 0xFF00FF00, Biome.OVERWORLD);
    public static final Tile WATER = new AnimatedTile(3, new int[][]{{0,5}, {1,5}, {2,5}, {3,5}, {4,5}, {5,5}, {4,5}, {3,5}, {2,5}, {1,5}}, Colors.get(-1, 004, 115, -1), 0xFF0000FF, 500, Biome.SEA);
    public static final Tile BAD = new BasicDamagingTile(4, 1,0, Colors.get(-1, 200, -1, -1), 0xFFff0000, 10, true);

    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    protected boolean damaging;
    protected boolean constantDamaging;
    protected int damageAmount;
    private BufferedImage sprite;
    private Biome biome;
    private SoundType soundType;

    public Tile(int id, boolean isSolid, boolean isEmitter, boolean isDamaging, boolean isConstantDamaging, int damageAmount, BufferedImage sprite, SoundType soundType, Biome biome) {
        this.id = (byte) id;
        if (tiles[id] != null) {
            JOptionPane.showMessageDialog(null, "Error:\nDuplicated tile id on " + id, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Duplicated tile id on " + id);
        }
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.damaging = isDamaging;
        this.constantDamaging = isConstantDamaging;
        this.damageAmount = damageAmount;
        this.sprite = sprite;
        this.biome = biome;
        if(soundType == null) this.soundType = SoundType.CLASSIC;
        else this.soundType = soundType;
        tiles[id] = this;
    }


    public byte getId() {
        return id;
    }
    public boolean isSolid() {
        return solid;
    }
    public boolean isEmitter() {
        return emitter;
    }
    public boolean isDamaging() {return damaging;}
    public boolean isConstantDamaging() {return constantDamaging;}
    public int getDamageAmount() {return damageAmount;}
    public SoundType getSoundType() {return soundType;}
    public Biome getBiome() {return biome;}


    public abstract void tick();

    public abstract void render(Screen screen, Level level, int x, int y);
}
