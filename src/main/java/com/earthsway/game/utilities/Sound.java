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
        else playSound(soundType, volume, -0);
    }

    private void pauseClip(SoundType soundType) {
        clips.put(soundType, new ClipData(clips.get(soundType).clip, clips.get(soundType).clip.getMicrosecondPosition(), clips.get(soundType).volume));
        clips.get(soundType).clip.stop();
    }

    private void resumeClip(SoundType soundType) {
        playSound(soundType, -0f, clips.get(soundType).clipTime);
    }

    public static void stopAllSounds() {
        for(ClipData c : clips.values()){
            c.clip.close();
        }
        clips = new HashMap<>();
    }

    //Convert all audio clips in Audacity

    private void playSound(SoundType soundType, float volume, long time){
        try {
            if(clips.containsKey(soundType) && clips.get(soundType) != null) closeClip(soundType);

            //if(clips.get(soundType) != null) volume = clips.get(soundType).volume;

            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundType.getAsResource(0));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            if(time != -0) clip.setMicrosecondPosition(time);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            if(volume != -0f){
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);
            }else{
                volume = ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getValue();
            }
            clips.put(soundType, new ClipData(clip, time, volume));
        } catch (Exception e) {
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
