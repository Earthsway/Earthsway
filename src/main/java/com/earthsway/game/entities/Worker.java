package com.earthsway.game.entities;

import com.earthsway.game.entities.utilities.*;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;

public class Worker extends Mob {

    private int color = Colors.get(-1, 180, 145, 543);
    private int[] movement;
    private int xa = 0;
    private int ya = 0;

    public Worker(Level level, int x, int y) {
        super(level, null, x, y, new int[]{0,6,3,6}, new Coords(100, 100),1, false, 1,
                new Health(100), new Shield(0, 0, 100), false, false, 1, true, EntityType.WORKER);
    }

    public void tick(){
        movement = randomMovementAI(x, y, xa, ya, this.tickCount);

        this.xa = movement[0];
        this.ya = movement[1];

        moveMob(xa, ya, this);
        super.tick();
    }

    public void render(Screen screen) {
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
    }
}
