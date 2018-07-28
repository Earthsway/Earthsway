package com.earthsway.game.utilities;

import com.earthsway.Utilities;
import com.earthsway.game.Main;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sound {

    private static Clip mainMenuClip;
    //Volume 0f - 1f
    //Convert all audio clips in Audacity
    public static void main_menu(boolean play, float volume) {
        try {
            if(!play){mainMenuClip.close(); return;}
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/sounds/main_menu.wav"));
            mainMenuClip = AudioSystem.getClip();
            mainMenuClip.open(inputStream);
            mainMenuClip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl floatControl = (FloatControl) mainMenuClip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            //Thread.sleep(10000);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            Utilities.errorReport(e, Sound.class);
        }
    }
}
