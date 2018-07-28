package com.earthsway.game.utilities;

public class Shield {
    protected int minShield;
    protected int maxShield;
    protected int currentShield;

    public int getMinShield() {
        return this.minShield;
    }
    public int getMaxShield() {
        return this.maxShield;
    }
    public int getCurrentShield() {return this.currentShield;}

    public void setMinShield(int minShield) {this.minShield = minShield;}
    public void setMaxShield(int maxShield) {this.maxShield = maxShield;}
    public void setCurrentShield(int currentShield) {this.currentShield = currentShield;}

    public Shield addToCurrentShield(int shield) {this.currentShield = this.currentShield + shield; return this;}
    public Shield subtractFromCurrentShield(int shield) {this.currentShield = this.currentShield - shield; return this;}

    public Shield(int minShield, int currentShield, int maxShield){
        this.minShield = minShield;
        this.currentShield = currentShield;
        this.maxShield = maxShield;
    }

    public Shield(int currentShield){
        this.minShield = 0;
        this.maxShield = currentShield;
        this.currentShield = currentShield;
    }
}
