package com.earthsway.game.utilities;

import com.earthsway.Utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.HashMap;
import java.util.Random;

public class Sound {
    public final static int caveSound = new Random().nextInt(100) + 1;


    private static HashMap<SoundType, ClipData> clips = new HashMap<>();

    public Sound(SoundType soundType, int index, float volume){
        if(soundType == null) stopAllSounds();
        else if(volume == 0f) closeClip(soundType);
        else if(volume == 0.01f) pauseClip(soundType);
        else if(volume == 0.02f) resumeClip(soundType, index);
        else playSound(soundType, index, volume, -0);
    }
    public Sound(SoundType soundType, float volume){
        if(soundType == null) stopAllSounds();
        else if(volume == 0f) closeClip(soundType);
        else if(volume == 0.01f) pauseClip(soundType);
        else if(volume == 0.02f) resumeClip(soundType, 0);
        else playSound(soundType, 0, volume, -0);
    }

    private void pauseClip(SoundType soundType) {
        clips.put(soundType, new ClipData(clips.get(soundType).clip, clips.get(soundType).clip.getMicrosecondPosition(), clips.get(soundType).volume));
        clips.get(soundType).clip.stop();
    }

    private void resumeClip(SoundType soundType, int index) {
        playSound(soundType, index, clips.get(soundType).volume, clips.get(soundType).clipTime);
    }

    public static void stopAllSounds() {
        for(ClipData c : clips.values()){
            c.clip.close();
        }
        clips = new HashMap<>();
    }

    //Convert all audio clips in Audacity

    private void playSound(SoundType soundType, int index, float volume, long time) {
        try {
            if (clips.containsKey(soundType) && clips.get(soundType) != null) closeClip(soundType);

            Clip clip;
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundType.getAsResource(index));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            if (time != -0) clip.setMicrosecondPosition(time);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = floatControl.getMaximum() - floatControl.getMinimum();
            float gain = (range * volume) + floatControl.getMinimum();
            floatControl.setValue(gain);

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
