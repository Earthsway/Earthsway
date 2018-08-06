package com.earthsway.game.utilities;

import com.earthsway.Utilities;
import com.earthsway.game.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;

public class Sound {

    private static HashMap<SoundType, ClipData> clips = new HashMap<>();

    public Sound(SoundType soundType, float volume){
        if(soundType == null) stopAllSounds();
        else if(volume == 0f) closeClip(soundType);
        else if(volume == 0.01f) pauseClip(soundType);
        else if(volume == 0.02f) resumeClip(soundType);
        else if(soundType == SoundType.CLASSIC)inGameClassic(volume);
        else if(soundType == SoundType.MAIN_MENU)main_menu(volume);
        else if(soundType == SoundType.CAVE)inGameCave(volume);
    }

    private void pauseClip(SoundType soundType) {
            clips.get(soundType).clip.stop();
        clips.put(soundType, new ClipData(clips.get(soundType).clip, clips.get(soundType).clip.getMicrosecondPosition()));
    }

    private void resumeClip(SoundType soundType) {

        clips.get(soundType).clip.start();
        clips.get(soundType).clip.setMicrosecondPosition(clips.get(soundType).clipTime);
            clips.put(soundType, new ClipData(clips.get(soundType).clip, clips.get(soundType).clip.getMicrosecondPosition()));
    }

    public static void stopAllSounds() {
        for(ClipData c : clips.values()){
            c.clip.close();
        }
        clips = new HashMap<>();
    }

    //Volume 0f - 1f
    //Convert all audio clips in Audacity
    private void main_menu(float volume) {
        try {
            if(clips.containsKey(SoundType.MAIN_MENU) && clips.get(SoundType.MAIN_MENU) != null) closeClip(SoundType.MAIN_MENU);
            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/sounds/main_menu.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            clips.put(SoundType.CLASSIC, new ClipData(clip));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            Utilities.errorReport(e, Sound.class);
        }
    }

    private void inGameClassic(float volume) {
        try {
            if(clips.containsKey(SoundType.CLASSIC) && clips.get(SoundType.CLASSIC) != null)closeClip(SoundType.CLASSIC);
            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/sounds/music1.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            clips.put(SoundType.CLASSIC, new ClipData(clip));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            Utilities.errorReport(e, Sound.class);
        }
    }

    private void inGameBattle(float volume) {
        try {
            if(clips.containsKey(SoundType.BATTLE) && clips.get(SoundType.BATTLE) != null)closeClip(SoundType.BATTLE);
            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/sounds/battle1.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            clips.put(SoundType.BATTLE, new ClipData(clip));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            Utilities.errorReport(e, Sound.class);
        }
    }

    private void inGameCave(float volume) {
        try {
            if(clips.containsKey(SoundType.CAVE) && clips.get(SoundType.CAVE) != null)closeClip(SoundType.CAVE);
            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/sounds/cave.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            clips.put(SoundType.CAVE, new ClipData(clip));
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            Utilities.errorReport(e, Sound.class);
        }
    }

    public static void closeClip(SoundType musicType){
        if(clips.containsKey(musicType)){
            clips.get(musicType).clip.close();
            clips.put(musicType, null);
        }
    }
}
