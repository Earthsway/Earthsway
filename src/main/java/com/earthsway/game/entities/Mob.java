package com.earthsway.game.entities;

import com.earthsway.game.level.Level;
import com.earthsway.game.level.tiles.Tile;

public abstract class Mob extends Entity{

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int movingDir = 1;
    protected int scale = 1;
    protected boolean canMoveDiagonal;
    protected int health = 100;
    protected double hitCooldown = 0.00;

    public Mob(Level level, String name, int x, int y, int speed) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.canMoveDiagonal = false;
        this.scale = 1;
    }

    public Mob(Level level, String name, int x, int y, int speed, boolean canMoveDiagonal) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.canMoveDiagonal = canMoveDiagonal;
        this.scale = 1;

    }

    public Mob(Level level, String name, int x, int y, int speed, boolean canMoveDiagonal, int scale) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.canMoveDiagonal = canMoveDiagonal;
        this.scale = scale;
    }

    public Mob(Level level, String name, int x, int y, int speed, boolean canMoveDiagonal, int scale, int health) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.canMoveDiagonal = canMoveDiagonal;
        this.scale = scale;
        this.health = health;
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
            if (damaged() && this.hitCooldown <= 0.00) {
                this.hitCooldown = 1.00;
                this.health -= 5;
            }
        } else if (damagedSolid(xa, ya) && this.hitCooldown <= 0.00) {
            this.hitCooldown = 1.00;
            this.health -= 5;
        }
    }

    public abstract boolean hasCollided(int xa, int ya);
    public abstract boolean damagedSolid(int xa, int ya);
    public abstract boolean damaged();

    protected boolean isSolidTile(int xa, int ya, int x, int y){
        if(level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        return !lastTile.equals(newTile) && newTile.isSolid();
    }

    protected boolean isSolidDamagingTile(int xa, int ya, int x, int y){
        if(level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        return !lastTile.equals(newTile) && newTile.isDamaging();
    }

    protected boolean isDamagingTile(int x, int y){
        if(level == null) return false;
        return level.getTile((this.x + x) >> 3, (this.y + y) >> 3).isDamaging();
    }

    /*protected boolean isSolidDamagingTile(int xa, int ya, int x, int y){
        if(level == null) return false;
        Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
        Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
        return !lastTile.equals(newTile) && newTile.isDamaging();
    }*/



    public String getName() {return name;}
    public int getNumSteps() {return numSteps;}
    public boolean isMoving() {return isMoving;}
    public int getMovingDir() {return movingDir;}
    public int getHealth() {return health;}

    public void setNumSteps(int numSteps) {this.numSteps = numSteps;}
    public void setMoving(boolean moving) {isMoving = moving;}
    public void setMovingDir(int movingDir) {this.movingDir = movingDir;}
}
