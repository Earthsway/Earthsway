package com.earthsway.game.entities;

import com.earthsway.game.InputHandler;
import com.earthsway.game.Main;
import com.earthsway.game.entities.utilities.*;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Font;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.level.tiles.Tile;
import com.earthsway.game.net.packets.Packet02Move;

public class Player extends Mob{

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    private int tickCount = 0;
    private String username;


    public Player(Level level, int x, int y, InputHandler input, int scale, String username) {
        super(level, "Player", x, y, new Coords(100, 100),1, true, scale,
                new Health(100), new Shield(0, 0, 100), false, true, 1, true, EntityType.PLAYER);
        this.input = input;
        this.scale = scale;
        this.username = username;
    }

    public void tick() {
        //this.color = Colors.get(-1, 180, 145, 543); for worker sprite
        this.coords = new Coords(this.x, this.y);
        super.tick();
        int xa = 0;
        int ya = 0;
        if(input != null) {
            if (input.up.isPressed()) ya--;
            if (input.down.isPressed()) ya++;
            if (input.left.isPressed()) xa--;
            if (input.right.isPressed()) xa++;
        }
        if(xa != 0 || ya != 0){
            move(xa,ya);
            isMoving = true;
            Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
            packet.writeData(Main.main.socketClient);
        }else isMoving = false;

        if(this.health.getCurrentHealth() <= this.health.getMinHealth()){
            System.out.println("You Have Died");
            respawn();
        }
        tickCount++;
    }

    @Override
    protected void respawn() {
        super.respawn();
        Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
        packet.writeData(Main.main.socketClient);
    }

    public void render(Screen screen) {
        /** xTile & yTile are used to define the top left tile of the defined sprite.*/
        int xTile = 0;
        int yTile = 27;

        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if(movingDir == MovingDirection.DOWN){
            xTile += 2;
        }else if(movingDir > 1){
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1)%2;
        }

        int modifier = 8*scale;
        int xOffset = x - modifier/2;
        int yOffset = y - modifier/2 - 4;

        if(this.swimming){
            int waterColor;
            yOffset += 4;
            if(tickCount % 60 < 15)waterColor = Colors.get(-1, -1, 225, -1);
            else if(15 <= tickCount % 60 && tickCount % 60 < 30){ waterColor = Colors.get(-1, 225, 115, -1);  yOffset -= 1;}
            else if(30 <= tickCount % 60 && tickCount % 60 < 45) waterColor = Colors.get(-1, 115, -1, 225);
            else{waterColor = Colors.get(-1, 225, 115, -1); yOffset -= 1;}

            screen.render(xOffset, yOffset + 3, 26 * 32, waterColor, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 26 * 32, waterColor, 0x01, 1);
        }

        screen.render(xOffset + (modifier* flipTop), yOffset, xTile + yTile*32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier* flipTop), yOffset, (xTile + 1) + yTile*32, color,flipTop, scale);

        if(!this.swimming){
        screen.render(xOffset  + (modifier* flipBottom), yOffset + modifier, xTile + (yTile + 1)*32, color, flipBottom, scale);
        screen.render(xOffset + modifier - (modifier* flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)*32, color, flipBottom, scale);
        }

        if(username != null){
            Font.render(username, screen, xOffset - ((username.length() -1 )/ 2 * 8), yOffset - 10, Colors.get(-1, -1, -1, 555));
        }
    }

    private final int collisionxMin = 0; //def: 0
    private final int collisionxMax = 6; //def: 6
    private final int collisionyMin = 3; //def: 3
    private final int collisionyMax = 6; //def: 6

    public boolean hasCollided(int xa, int ya) {
        for(int x = collisionxMin; x < collisionxMax; x++){if(isSolidTile(xa,ya,x,collisionyMin)) return true;}
        for(int x = collisionxMin; x < collisionxMax; x++){if(isSolidTile(xa,ya,x,collisionyMax)) return true;}
        for(int y = collisionyMin; y < collisionyMax; y++){if(isSolidTile(xa,ya,collisionxMin,y)) return true;}
        for(int y = collisionyMin; y < collisionyMax; y++){if(isSolidTile(xa,ya,collisionxMax,y)) return true;}
        return false;
    }

    public Tile damagedSolid(int xa, int ya) {
        for(int x = collisionxMin; x < collisionxMax; x++){Tile t = isSolidDamagingTile(xa,ya,x,collisionyMin); if(t != null){return t;}}
        for(int x = collisionxMin; x < collisionxMax; x++){Tile t = isSolidDamagingTile(xa,ya,x,collisionyMax); if(t != null){return t;}}
        for(int y = collisionyMin; y < collisionyMax; y++){Tile t = isSolidDamagingTile(xa,ya,collisionxMin,y); if(t != null){return t;}}
        for(int y = collisionyMin; y < collisionyMax; y++){Tile t = isSolidDamagingTile(xa,ya,collisionxMax,y); if(t != null){return t;}}
        return null;
    }

    public void updateTiles() {
        for(int x = collisionxMin; x < collisionxMax; x++){this.onTiles[0] = level.getTile((this.x + x) >> 3, (this.y + collisionyMin) >> 3);}
        for(int x = collisionxMin; x < collisionxMax; x++){this.onTiles[1] = level.getTile((this.x + x) >> 3, (this.y + collisionyMax) >> 3);}
        for(int y = collisionyMin; y < collisionyMax; y++){this.onTiles[2] = level.getTile((this.x + collisionxMin) >> 3, (this.y + y) >> 3);}
        for(int y = collisionyMin; y < collisionyMax; y++){this.onTiles[3] = level.getTile((this.x + collisionxMax) >> 3, (this.y + y) >> 3);}
    }

    public boolean isSwimming() {return this.swimming;}
    public String getUsername(){return this.username;}
    public Health getHealth() {return this.health;}


}