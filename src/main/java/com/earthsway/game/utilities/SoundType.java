package com.earthsway.game.utilities;

import com.earthsway.game.Main;

import java.net.URL;
import java.util.ResourceBundle;

public enum SoundType {
    BATTLE(false, null),
    MAIN_MENU(false, "/sounds/main_menu.wav"),
    CAVE(true, "/sounds/cave.wav"),
    CLASSIC(true, "/sounds/music1.wav");

    String[] sounds;
    boolean init;
    SoundType(boolean init, String... sounds) {this.sounds = sounds; this.init = init;}
    public boolean shouldInit(){return init;}
    public URL getAsResource(int index) {return Main.class.getResource(sounds[index]);}
    public String[] getSounds() {return sounds;}
}
