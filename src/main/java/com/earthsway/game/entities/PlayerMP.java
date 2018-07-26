package com.earthsway.game.entities;

import com.earthsway.game.InputHandler;
import com.earthsway.game.level.Level;

import java.net.InetAddress;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port) {
        super(level, x, y, input, 1, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port) {
        super(level, x, y, null, 1, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick(){
        super.tick();
    }

}
