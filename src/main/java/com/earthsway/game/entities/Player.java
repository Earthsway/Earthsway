package com.earthsway.game.entities;

import com.earthsway.game.InputHandler;
import com.earthsway.game.entities.utilities.MovingDirection;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;

public class Player extends Mob{

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    private int scale;

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

        screen.render(xOffset + (modifier* flipTop), yOffset, xTile + yTile*32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier* flipTop), yOffset, (xTile + 1) + yTile*32, color,flipTop, scale);

        screen.render(xOffset  + (modifier* flipBottom), yOffset + modifier, xTile + (yTile + 1)*32, color, flipBottom, scale);
        screen.render(xOffset + modifier - (modifier* flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)*32, color, flipBottom, scale);
    }

    public boolean hasCollided(int xa, int ya) {
        return false;
    }

}
