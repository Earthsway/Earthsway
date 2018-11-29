package com.earthsway.game.entities;

import com.earthsway.game.Game;
import com.earthsway.game.utilities.*;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.level.Level;
import com.earthsway.game.level.Node;

import java.awt.*;
import java.util.List;

public class Worker extends Mob {

    private int color = Colors.get(-1, 180, 145, 543);
    //private int[] movement;
    private int xa = 0;
    private int ya = 0;
    private List<Node> path = null;
    private int time = 0;
    private boolean followPlayer;

    public Worker(Level level, int x, int y, boolean followPlayer) {
        super(level, null, x, y, new int[]{0,6,3,6}, new Coords(100, 100),1, false, 2,
                new Health(100), new Shield(0, 0, 100), false, true, 1, true, EntityType.WORKER);
        this.followPlayer = followPlayer;
    }

    public void tick() {
        this.color = Colors.get(-1, 180, 1, 543);
        //movement = randomMovementAI(x, y, xa, ya, this.tickCount);
        //this.xa = movement[0];
        //this.ya = movement[1];
        if (followPlayer) {
            followMovementAI(this.x, this.y, Game.main.player.x, Game.main.player.y, xa, ya, this.speed, this, path, time);
            moveMob(xa, ya, this);
        }
        super.tick();
    }

    public void render(Graphics g) {
        time++;
        int xTile = 0;
        int yTile = 25;

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

        this.renderWaterSplash(screen, xOffset, yOffset, modifier,flipTop, flipBottom,xTile,yTile, color);

        screen.render(xOffset + (modifier* flipTop), yOffset, xTile + yTile*32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier* flipTop), yOffset, (xTile + 1) + yTile*32, color,flipTop, scale);

    }
}
