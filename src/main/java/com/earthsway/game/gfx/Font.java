package com.earthsway.game.gfx;

public class Font {
    final private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
                                        "0123456789.,:;'\"!?$%()-=+/      ";

    public static void render(String msg, Screen screen, int x, int y, int color){
        render(msg, screen, x, y, color, 1, false);

    }

    public static void render(String msg, Screen screen, int x, int y, int color, int scale){
        render(msg, screen, x, y, color,1, false);
    }

    public static void render(String msg, Screen screen, int x, int y, int color, int scale, boolean centered){
        msg = msg.toUpperCase();
        if(centered){
            x = x - (msg.length()*8)/2;
        }
        for(int i = 0; i < msg.length(); i++){
            int charIndex = chars.indexOf(msg.charAt(i));
            if(charIndex >= 0) screen.render(x + (i*8),y, charIndex + 30 * 32, color, 0x00, scale);
        }
    }

}
