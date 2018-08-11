package com.earthsway.game.utilities;

import com.earthsway.game.Main;

import java.net.URL;

public enum SoundType {
    @SuppressWarnings({"all"})
    //Initializing Only Required For Large Clips.
    MAIN_MENU(false, true, "/sounds/main_menu.wav"),
    BATTLE(false, true, ""),
    CAVE(true, true, "/sounds/cave1.wav", "/sounds/cave2.wav"),
    CLASSIC(true, true, "/sounds/music1.wav"),
    SHIELD_POTION_DRINK(true, false, "/sounds/shieldPotionDrink.wav"),
    TILE_STONE_STEP(true, false, "/sounds/tile/stone/step.wav"),
    ;

    String[] sounds;
    boolean init;
    boolean loop;

    SoundType(boolean init, boolean loop, String... sounds) {
        this.sounds = sounds;
        this.init = init;
        this.loop = loop;
    }
    public boolean shouldInit(){return init;}
    public boolean shouldLoop(){return loop;}
    public URL getAsResource(int index) {return Main.class.getResource(sounds[index]);}
    public String[] getSounds() {return sounds;}

    public String getSound(int index) {return getSounds()[index +-1];}

}
