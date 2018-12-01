package com.earthsway.game.gfx;

import com.earthsway.Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SpriteSheet {
    private int sheetTileSize;

    private BufferedImage sheet;
    public SpriteSheet(String path, int sheetTileSize) {
        this.sheetTileSize = sheetTileSize;
        try {this.sheet = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));}
        catch (Exception e) {e.printStackTrace();Utilities.errorReport(e, getClass());}
    }

    public BufferedImage getSprite(int column, int row){
        return sheet.getSubimage(column*sheetTileSize, row*sheetTileSize, (column*sheetTileSize) + sheetTileSize, (row*sheetTileSize) + sheetTileSize);
    }
    public BufferedImage getSprite(int column, int row, int columnAdd, int rowAdd){
        return sheet.getSubimage(column*sheetTileSize, row*sheetTileSize, (columnAdd*sheetTileSize) + sheetTileSize, (rowAdd*sheetTileSize) + sheetTileSize);
    }
}
