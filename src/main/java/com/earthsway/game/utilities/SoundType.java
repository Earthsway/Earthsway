package com.earthsway.game.utilities;

import com.earthsway.game.Main;

import java.net.URL;

public enum SoundType {
    @SuppressWarnings({"all"})
    //Initializing Only Required For Large Clips.
    BATTLE(false, null),
    MAIN_MENU(false, "/sounds/main_menu.wav"),
    CAVE(true, "/sounds/cave1.wav", "/sounds/cave2.wav"),
    CLASSIC(true, "/sounds/music1.wav");

    //TODO be able to call what ever sound wanted

    String[] sounds;
    boolean init;
    SoundType(boolean init, String... sounds) {this.sounds = sounds; this.init = init;}
    public boolean shouldInit(){return init;}
    public URL getAsResource(int index) {return Main.class.getResource(sounds[index]);}
    public String[] getSounds() {return sounds;}

    public String getSound(int index) {return getSounds()[index +-1];}

}
