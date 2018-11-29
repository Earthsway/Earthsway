package com.earthsway.game.gfx;

import java.awt.*;

public class Font {
    final private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
                                        "abcdefghijklmnopqrstuvwxyz      " +
                                        "0123456789.,:;'\"!?$%()-=+/      ";

    public static void render(String msg, Graphics g, int x, int y, int color){
        render(msg, g, x, y, color, 1, false);

    }

    public static void render(String msg, Graphics g, int x, int y, int color, int scale){
        render(msg, g, x, y, color,1, false);
    }

    public static void render(String msg, Graphics g, int x, int y, int color, int scale, boolean centered){
        if(centered) {
            x = x - (msg.length() * 8) / 2;
        }
        for(int i = 0; i < msg.length(); i++){
            int charIndex = chars.indexOf(msg.charAt(i));
            if(charIndex >= 0) screen.render(x + (i*8),y, charIndex + 29 * 32, color, 0x00, scale);
        }
    }

}
