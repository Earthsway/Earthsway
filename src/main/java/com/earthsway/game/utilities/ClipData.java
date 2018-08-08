package com.earthsway.game.utilities;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class ClipData {
    protected Clip clip;
    protected long clipTime;
    protected float volume;

    /*public ClipData(Clip clip){
        if (clip != null) {
            this.clip = clip;
            if(clip.getMicrosecondPosition() > 0) {
                this.clipTime = clip.getMicrosecondPosition();
            }
            if(((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).getValue() != -0f) {
                this.volume = ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).getValue();
            }

        }
    }*/

    public ClipData(Clip clip, long clipTime, float volume){
        this.clip = clip;
        this.clipTime = clipTime;
        this.volume = volume;
    }
}
