package com.earthsway.game.utilities;

import javax.sound.sampled.Clip;

public class ClipData {
    protected Clip clip;
    protected long clipTime;
    public ClipData(Clip clip){
        this.clip = clip;
        this.clipTime = 1;
    }
    public ClipData(Clip clip, long clipTime){
        this.clip = clip;
        this.clipTime = clipTime;
    }

}
