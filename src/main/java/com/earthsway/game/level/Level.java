package com.earthsway.game.level;

import com.earthsway.Utilities;
import com.earthsway.game.entities.Entity;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private byte[] tiles;
    public int width;
    public int height;
    public List<Entity> entities = new ArrayList<Entity>();

    private BufferedImage image;
    private String imagePath;

    public Level(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            tiles = new byte[width * height];
            this.width = 64;
            this.height = 64;
            this.generateLevel();
        }
    }

    private void loadLevelFromFile(){
        try {
            this.image = ImageIO.read(Level.class.getResource(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    private void loadTiles() {
        int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck: for(Tile t : Tile.tiles){
                    if(t != null && t.getLevelColor() == tileColors[x + y * width]){
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    private void saveLevelToFile(){
        try{
            ImageIO.write(this.image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    public void alterTile(int x, int y, Tile newTile){
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }

    private void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 8/* generate amount*/) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                } else {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public void tick(){
        for(Entity e : entities){
            e.tick();
        }
        for(Tile t : Tile.tiles){
            if(t == null) break;
            else t.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) xOffset = 0;
        if (xOffset > ((width << 3) - screen.width)) xOffset = ((width << 3) - screen.width);
        if (yOffset < 0) yOffset = 0;
        if (yOffset > ((height << 3) - screen.height)) yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                getTile(x,y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen){
        for(Entity e : entities){
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if(0 > x || x >= width || 0 > y || y >= height) return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
    }

    public void addEntity(Entity entity){
        this.entities.add(entity);
    }

}
