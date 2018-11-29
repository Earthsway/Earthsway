package com.earthsway.game.gfx;

import com.earthsway.game.Game;
import com.earthsway.game.entities.Player;

import java.awt.*;

public class Hud {
    private Graphics graphics;
    private Player player;

    public Hud(Graphics graphics){
        this.graphics = graphics;
        this.player = Game.main.player;
        healthBar();
        shieldBar();
    }

    private void healthBar(){
        int healthScale = player.getHealth().getCurrentHealth() * 10 / 5;
        graphics.setColor(Color.GREEN);
        graphics.fillRect(60, 760, healthScale, 10);
    }

    private void shieldBar() {
        if(player.getShield().getCurrentShield() > 0) {
            int healthScale = player.getShield().getCurrentShield() * 10 / 5;
            graphics.setColor(Color.BLUE);
            graphics.fillRect(60, 720, healthScale, 10);
        }
    }
}
