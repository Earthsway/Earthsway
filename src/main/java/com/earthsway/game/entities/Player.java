package com.earthsway.game.entities;

import com.earthsway.game.InputHandler;
import com.earthsway.game.entities.utilities.MovingDirection;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.level.tiles.Tile;

import java.awt.*;

public class Player extends Mob{

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    private int scale;
    protected boolean isSwimming = false;
    private int tickCount = 0;


    public Player(Level level, int x, int y, InputHandler input, int scale) {
        super(level, "Player", x, y, 1, true, scale);
        this.input = input;
        this.scale = scale;
    }

    public Player(Level level, int x, int y, InputHandler input) {
        super(level, "Player", x, y, 1, true, 1);
        this.input = input;
        this.scale = 1;
    }

    public void tick() {
        int xa = 0;
        int ya = 0;

        if(input.up.isPressed()) ya--;
        if(input.down.isPressed()) ya++;
        if(input.left.isPressed()) xa--;
        if(input.right.isPressed()) xa++;

        if(xa != 0 || ya != 0){
            move(xa,ya);
            isMoving = true;
        }else isMoving = false;
        if(level.getTile(this.x >> 3, this.y >> 3).getId() == Tile.WATER.getId()) isSwimming = true;
        if(isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != Tile.WATER.getId()) isSwimming = false;
        tickCount++;
    }

    public void render(Screen screen) {
        /**
         * xTile & yTile are used to define the top left tile of the defined sprite.
         */
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

        if(isSwimming){
            int waterColor;
            yOffset += 4;
            if(tickCount % 60 < 15) waterColor = Colors.get(-1, -1, 225, -1);
            else if(15 <= tickCount % 60 && tickCount % 60 < 30) waterColor = Colors.get(-1, 225, 115, -1);
            else if(30 <= tickCount % 60 && tickCount % 60 < 45) waterColor = Colors.get(-1, 115, -1, 225);
            else waterColor = Colors.get(-1, 225, 115, -1);

            screen.render(xOffset, yOffset + 3, 27 * 32, waterColor, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 27 * 32, waterColor, 0x01, 1);
        }

        screen.render(xOffset + (modifier* flipTop), yOffset, xTile + yTile*32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier* flipTop), yOffset, (xTile + 1) + yTile*32, color,flipTop, scale);

        if(!isSwimming){
        screen.render(xOffset  + (modifier* flipBottom), yOffset + modifier, xTile + (yTile + 1)*32, color, flipBottom, scale);
        screen.render(xOffset + modifier - (modifier* flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)*32, color, flipBottom, scale);
        }
    }

    public boolean hasCollided(int xa, int ya) {
        int xMin = 0; //def: 0
        int xMax = 6; //def: 7
        int yMin = 3; //def: 3
        int yMax = 6; //def: 7

        for(int x = xMin; x < xMax; x++){if(isSolidTile(xa,ya,x,yMin)) return true;}
        for(int x = xMin; x < xMax; x++){if(isSolidTile(xa,ya,x,yMax)) return true;}
        for(int y = yMin; y < yMax; y++){if(isSolidTile(xa,ya,xMin,y)) return true;}
        for(int y = yMin; y < yMax; y++){if(isSolidTile(xa,ya,xMax,y)) return true;}

        return false;
    }

    public boolean isSwimming() {return isSwimming;}
}
