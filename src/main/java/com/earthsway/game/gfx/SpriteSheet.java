package com.earthsway.game.gfx;

import com.earthsway.Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SpriteSheet {
    public String path;
    public int width;
    public int height;

    public int[] pixels;

    public SpriteSheet(String path){
        BufferedImage image = null;
        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }

        if(image == null) return;

        this.path = path;
        this.width = image.getWidth();
        this.height = image.getHeight();

        pixels = image.getRGB(0,0,width, height, null, 0, width);

        for (int i = 0; i < pixels.length; i++){
            pixels[i] = (pixels[i] & 0xFF)/64;
        }
    }
}
