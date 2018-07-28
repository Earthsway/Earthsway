package com.earthsway.game.entities;

import com.earthsway.game.utilities.*;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.level.Node;
import com.earthsway.game.level.tiles.Tile;

import java.util.List;
import java.util.Random;

public abstract class Mob extends Entity{

    protected Random random = new Random();
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
    protected int[] collisionBox;
    protected int tickCount = 0;

    public Mob(Level level, String name, int x, int y, int[] collisionBox, Coords respawnCoords, int speed, boolean canMoveDiagonal, int scale, Health health, Shield shield, boolean respawnWithShield, boolean damageable, double hitCooldown, boolean canSwim, EntityType entityType) {
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
        this.collisionBox = collisionBox;
    }

    public void move(int xa, int ya) {
        //TODO Integrate damage/hit into diagonal
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
        this.tickCount++;
    }

    protected Tile shouldBeDamaged(){
        for (Tile tile : this.onTiles) {if (tile != null && tile.isDamaging()){return tile;}}
        return null;
    }

    protected boolean hasCollided(int xa, int ya) {
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){if(isSolidTile(xa,ya,x,this.collisionBox[2])) return true;}
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){if(isSolidTile(xa,ya,x,this.collisionBox[3])) return true;}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){if(isSolidTile(xa,ya,this.collisionBox[0],y)) return true;}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){if(isSolidTile(xa,ya,this.collisionBox[1],y)) return true;}
        return false;
    }

    protected Tile damagedSolid(int xa, int ya) {
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){Tile t = isSolidDamagingTile(xa,ya,x,this.collisionBox[2]); if(t != null){return t;}}
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){Tile t = isSolidDamagingTile(xa,ya,x,this.collisionBox[3]); if(t != null){return t;}}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){Tile t = isSolidDamagingTile(xa,ya,this.collisionBox[0],y); if(t != null){return t;}}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){Tile t = isSolidDamagingTile(xa,ya,this.collisionBox[1],y); if(t != null){return t;}}
        return null;
    }

    protected void updateTiles() {
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){this.onTiles[0] = level.getTile((this.x + x) >> 3, (this.y + this.collisionBox[2]) >> 3);}
        for(int x = this.collisionBox[0]; x < this.collisionBox[1]; x++){this.onTiles[1] = level.getTile((this.x + x) >> 3, (this.y + this.collisionBox[3]) >> 3);}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){this.onTiles[2] = level.getTile((this.x + this.collisionBox[0]) >> 3, (this.y + y) >> 3);}
        for(int y = this.collisionBox[2]; y < this.collisionBox[3]; y++){this.onTiles[3] = level.getTile((this.x + this.collisionBox[1]) >> 3, (this.y + y) >> 3);}
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

    protected void renderWaterSplash(Screen screen, int xOffset, int yOffset, int modifier, int flipTop, int flipBottom, int xTile, int yTile, int color){
        if(this.swimming){
            int waterColor;
            yOffset += 4;
            if(this.tickCount % 60 < 15)waterColor = Colors.get(-1, -1, 225, -1);
            else if(15 <= this.tickCount % 60 && this.tickCount % 60 < 30){ waterColor = Colors.get(-1, 225, 115, -1);  yOffset -= 1;}
            else if(30 <= this.tickCount % 60 && this.tickCount % 60 < 45) waterColor = Colors.get(-1, 115, -1, 225);
            else{waterColor = Colors.get(-1, 225, 115, -1); yOffset -= 1;}

            screen.render(xOffset + this.scale, yOffset + 3, 31 + 31 * 32, waterColor, 0x00, this.scale);
            screen.render(xOffset + 8 * this.scale, yOffset + 3, 31 + 31 * 32, waterColor, 0x01, this.scale);
        }

        if(!this.swimming){
            screen.render(xOffset  + (modifier* flipBottom), yOffset + modifier, xTile + (yTile + 1)*32, color, flipBottom, scale);
            screen.render(xOffset + modifier - (modifier* flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)*32, color, flipBottom, scale);
        }
    }

    protected void followMovementAI(int x, int y, int px, int py, double xa,
                                   double ya, double speed, Mob mob, List<Node> path, int time) {
        xa = 0;
        ya = 0;
        Vector2i start = new Vector2i(x >> 3, y >> 3);
        Vector2i goal = new Vector2i(px >> 3, py >> 3);
        path = level.findPath(start, goal);
        if(path != null) {
            if(path.size() > 0){
                Vector2i vector = path.get(path.size() - 1).tile;
                if(x < vector.getX() << 3) xa =+ speed;
                if(x > vector.getX() << 3) xa =- speed;
                if(y < vector.getY() << 3) ya =+ speed;
                if(y > vector.getY() << 3) ya =- speed;
                moveMob((int)xa, (int)ya, mob);
            }
        }
    }

    protected int[] randomMovementAI(int x, int y, int xa, int ya, int tick) {
        if (tick % (random.nextInt(50) + 30) == 0) {
            xa = random.nextInt(3) - 1;
            ya = random.nextInt(3) - 1;
            if (random.nextInt(4) == 0) {
                xa = 0;
                ya = 0;
            }
        }
        /*if(x <= 180){
            xa = 1;
            ya = -1;
        }*/ //TODO will this affect
        int move[] = new int[2];
        move[0] = xa;
        move[1] = ya;
        return move;
    }

    protected void moveMob(int xa, int ya, Mob mob) {
        if (xa != 0 || ya != 0) {
            mob.move(xa, ya);
            mob.isMoving = true;
        } else {
            mob.isMoving = false;
        }
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
