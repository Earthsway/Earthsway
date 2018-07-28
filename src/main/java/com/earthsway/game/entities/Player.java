package com.earthsway.game.entities;

import com.earthsway.game.InputHandler;
import com.earthsway.game.Main;
import com.earthsway.game.utilities.*;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Font;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.Level;
import com.earthsway.game.net.packets.Packet02Move;

public class Player extends Mob{

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    private String username;

    public Player(Level level, int x, int y, InputHandler input, int scale, String username) {
        super(level, "Player", x, y, new int[]{0,6,3,6}, new Coords(100, 100),1, true, scale,
                new Health(100), new Shield(0, 0, 100), true, true, 1, true, EntityType.PLAYER);
        this.input = input;
        this.scale = scale;
        this.username = username;
    }

    public void tick() {
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

        if(this.health.getCurrentHealth() <= this.health.getMinHealth()) {
            System.out.println("You Have Died");
            respawn();
        }
        sendDiscordData();
    }

    @Override
    protected void respawn() {
        super.respawn();
        Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
        packet.writeData(Main.main.socketClient);
    }

    private void sendDiscordData() {
        if (this.shield.getCurrentShield() <= 0) Main.presence.details = "Health: " + this.health.getCurrentHealth() + "/" + this.health.getMaxHealth();
        else Main.presence.details = "Health: " + this.health.getCurrentHealth() + "/" + this.health.getMaxHealth() + " | "
                    + "Shield: " + this.shield.getCurrentShield() + "/" + this.shield.getMaxShield();
        /*int multiplayerAmount = 0;
        for(EntityType et : this.level.getEntityTypes()){
            if (et.equals(EntityType.PLAYER_MULTIPLAYER)) multiplayerAmount++;
        }
        if(multiplayerAmount > 0){
            Main.presence.state = "Playing With";
            Main.presence.partySize = multiplayerAmount;
            Main.presence.partyMax = multiplayerAmount;
        }
        else{
            Main.presence.state = "";
            Main.presence.partySize = 0;
            Main.presence.partyMax = 0;
        }*/
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

        this.renderWaterSplash(screen, xOffset, yOffset, modifier,flipTop, flipBottom,xTile,yTile, color);

        if(username != null){
            Font.render(username, screen, xOffset - ((username.length() -1 )/ 2 * 8), yOffset - 10, Colors.get(-1, -1, -1, 555));
        }
    }

    public boolean isSwimming() {return this.swimming;}
    public String getUsername(){return this.username;}
    public Health getHealth() {return this.health;}


}