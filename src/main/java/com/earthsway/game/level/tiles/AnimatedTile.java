package com.earthsway.game.level.tiles;

public class AnimatedTile extends BaseTile{

    private int[][] animationTileCoords;
    private int currentAnimationIndex;
    private long lastIterationTime;
    private int animationSwitchDelay;

    public AnimatedTile(int id, int[][] animationCoords, int tileColor, int levelColor, int animationSwitchDelay) {
        super(id, animationCoords[0][0], animationCoords[0][1], tileColor, levelColor);
        this.animationTileCoords = animationCoords;
        this.currentAnimationIndex = 0;
        this.lastIterationTime = System.currentTimeMillis();
        this.animationSwitchDelay = animationSwitchDelay;
        this.damaging = false;
    }


    public AnimatedTile(int id, int[][] animationCoords, int tileColor, int levelColor, int animationSwitchDelay, boolean damaging) {
        super(id, animationCoords[0][0], animationCoords[0][1], tileColor, levelColor);
        this.animationTileCoords = animationCoords;
        this.currentAnimationIndex = 0;
        this.lastIterationTime = System.currentTimeMillis();
        this.animationSwitchDelay = animationSwitchDelay;
        this.damaging = damaging;
    }

    public void tick(){
        if((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)){
            lastIterationTime = System.currentTimeMillis();
            currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
            tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1]*32));
        }
    }
}
