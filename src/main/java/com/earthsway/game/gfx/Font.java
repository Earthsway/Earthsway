package com.earthsway.game.gfx;

public class Font {
    final private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
                                        "abcdefghijklmnopqrstuvwxyz      " +
                                        "0123456789.,:;'\"!?$%()-=+/      ";

    public static void render(String msg, Screen screen, int x, int y, int color){
        render(msg, screen, x, y, color, 1, false);

    }

    public static void render(String msg, Screen screen, int x, int y, int color, int scale){
        render(msg, screen, x, y, color,1, false);
    }

    public static void render(String msg, Screen screen, int x, int y, int color, int scale, boolean centered){
        if(centered) {
            x = x - (msg.length() * 8) / 2;
        }
        for(int i = 0; i < msg.length(); i++){
            int charIndex = chars.indexOf(msg.charAt(i));
            if(charIndex >= 0) screen.render(x + (i*8),y, charIndex + 29 * 32, color, 0x00, scale);
        }
    }

}
