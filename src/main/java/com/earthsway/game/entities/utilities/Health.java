package com.earthsway.game.entities.utilities;

public class Health {
    protected int minHealth;
    protected int maxHealth;
    protected int currentHealth;

    public int getMinHealth() {
        return this.minHealth;
    }
    public int getMaxHealth() {
        return this.maxHealth;
    }
    public int getCurrentHealth() {return this.currentHealth;}

    public void setMinHealth(int minHealth) {this.minHealth = minHealth;}
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void setCurrentHealth(int currentHealth) {this.currentHealth = currentHealth;}

    public Health addToCurrentHealth(int health) {this.currentHealth = this.currentHealth + health; return this;}
    public Health subtractFromCurrentHealth(int health) {this.currentHealth = this.currentHealth - health; return this;}

    public Health(int minHealth, int currentHealth, int maxHealth){
        this.minHealth = minHealth;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
    }

    public Health(int currentHealth){
        this.minHealth = 0;
        this.maxHealth = currentHealth;
        this.currentHealth = currentHealth;
    }
}

