package com.earthsway.game.gfx;

import com.earthsway.utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    public String path;
    public int width;
    public int hight;

    public int[] pixels;

    public SpriteSheet(String path){
        BufferedImage image = null;
        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
            utilities.errorReport(e, getClass());
        }

        if(image == null) return;

        this.path = path;
        this.width = image.getWidth();
        this.hight = image.getHeight();

        pixels = image.getRGB(0,0,width,hight, null, 0, width);

        for (int i = 0; i < pixels.length; i++){
            pixels[i] = (pixels[i] & 0xFF)/64;
        }

        for(int i=0;i<64;i++){
            System.out.println(pixels[i]);
        }
    }
}
