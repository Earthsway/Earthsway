package com.earthsway.game.entities;

import com.earthsway.game.entities.utilities.Coords;
import com.earthsway.game.entities.utilities.EntityType;
import com.earthsway.game.entities.utilities.Health;
import com.earthsway.game.entities.utilities.Shield;
import com.earthsway.game.level.Level;
import com.earthsway.game.level.tiles.Tile;

public abstract class Mob extends Entity{

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int movingDir = 1;
    protected int scale;
    protected boolean canMoveDiagonal;
    protected Health health;
    protected Shield shield;
    protected double currentHitCooldown = 0.00;
    protected double hitCooldown;
    protected boolean damageable;
    protected Coords coords;
    protected Tile[] onTiles = new Tile[4];
    protected boolean canSwim;
    protected boolean swimming = false;
    protected boolean respawnWithShield;
    protected Coords respawnCoords;
    protected EntityType entityType;

    public Mob(Level level, String name, int x, int y, Coords respawnCoords, int speed, boolean canMoveDiagonal, int scale, Health health, Shield shield, boolean respawnWithShield, boolean damageable, double hitCooldown, boolean canSwim, EntityType entityType) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.canMoveDiagonal = canMoveDiagonal;
        this.scale = scale;
        this.health = health;
        this.shield = shield;
        this.damageable = damageable;
        this.canSwim = canSwim;
        this.entityType = entityType;
        this.hitCooldown = hitCooldown;
        this.respawnWithShield = respawnWithShield;
        this.respawnCoords = respawnCoords;
    }

    public void move(int xa, int ya) {
        if (!canMoveDiagonal && xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }
        numSteps++;
        if (!hasCollided(xa, ya)) {
            if (ya < 0) movingDir = 0;
            if (ya > 0) movingDir = 1;
            if (xa < 0) movingDir = 2;
            if (xa > 0) movingDir = 3;
            x += xa * speed;
            y += ya * speed;
            if (this.damageable && this.currentHitCooldown <= 0.00) {
                Tile t = shouldBeDamaged();
                if(t != null){
                    this.currentHitCooldown = this.hitCooldown;
                    damage(t.getDamageAmount());
                }
            }
        } else if (this.damageable && this.currentHitCooldown <= 0.00) {
            Tile t = damagedSolid(xa, ya);
            if(t != null){
                this.currentHitCooldown = this.hitCooldown;
                damage(t.getDamageAmount());
            }
        }
    }

    public void tick() {
        this.updateTiles();
        Tile t = null;
        for (Tile tile : this.onTiles) {
            if (tile.isConstantDamaging()){t = tile; break;}
        }
        if (t != null && this.currentHitCooldown <= 0) {
            this.currentHitCooldown = this.hitCooldown;
            damage(t.getDamageAmount());
        }
        if(canSwim) shouldSwim();

        this.currentHitCooldown -= 0.05;//default 0.05
    }

    public abstract boolean hasCollided(int xa, int ya);
    public abstract Tile damagedSolid(int xa, int ya);
    public abstract void updateTiles();

    protected Tile shouldBeDamaged(){
        for (Tile tile : this.onTiles) {if (tile.isDamaging()){return tile;}}
        return null;
    }

    protected boolean isSolidTile(int xa, int ya, int x, int y){
        if(level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        return !lastTile.equals(newTile) && newTile.isSolid();
    }

    protected Tile isSolidDamagingTile(int xa, int ya, int x, int y){
        if(level == null) return null;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        if(!lastTile.equals(newTile) && newTile.isDamaging()) return newTile;
        return null;
    }

    protected void shouldSwim(){
        int i = 0;
        for (Tile tile : this.onTiles) {if (tile.getId() == Tile.WATER.getId()){i++;}}
        this.swimming = i >= 4;
    }

    protected void damage(int amount){damage(amount, false);}
    protected void damage(int amount, boolean bypassShield) {
        if (bypassShield) this.health.subtractFromCurrentHealth(amount);
        else {
            int shield = this.shield.getCurrentShield() - amount;
            if (shield < 0) {
                this.health.subtractFromCurrentHealth(-shield);
                this.shield.setCurrentShield(0);
            } else {
                this.shield.subtractFromCurrentShield(amount);
            }
        }
    }

    protected void respawn(){
        this.x = respawnCoords.getX();
        this.y = respawnCoords.getY();
        this.health.setCurrentHealth(this.health.getMaxHealth());
        if(this.respawnWithShield) this.shield.setCurrentShield(this.shield.getMaxShield());
    }

    public String getName() {return name;}
    public Coords getCoords() {return coords;}
    public int getSpeed() {return speed;}
    public boolean canMoveDiagonal() {return canMoveDiagonal;}
    public int getScale() {return scale;}
    public boolean isDamageable() {return damageable;}
    public boolean canSwim() {return canSwim;}
    public EntityType getEntityType() {return entityType;}
    public int getNumSteps() {return numSteps;}
    public boolean isMoving() {return isMoving;}
    public int getMovingDir() {return movingDir;}
    public Health getHealth() {return health;}
    public Shield getShield() {return shield;}
    public double getHitCooldown() {return hitCooldown;}
    public double getCurrentHitCooldown() {return currentHitCooldown;}
    public boolean canRespawnWithShield() {return respawnWithShield;}

    public void setNumSteps(int numSteps) {this.numSteps = numSteps;}
    public void setMoving(boolean moving) {isMoving = moving;}
    public void setMovingDir(int movingDir) {this.movingDir = movingDir;}
    public void setHitCooldown(double hitCooldown) {this.hitCooldown = hitCooldown;}

}
