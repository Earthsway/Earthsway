package com.earthsway.game.utilities;

import javax.sound.sampled.Clip;

public class ClipData {
    protected Clip clip;
    protected long clipTime;
    protected float volume;

    public ClipData(Clip clip, long clipTime, float volume){
        this.clip = clip;
        this.clipTime = clipTime;
        this.volume = volume;
    }
}
