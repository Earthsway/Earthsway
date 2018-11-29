package com.earthsway.game;

import com.earthsway.game.net.packets.Packet01Disconnect;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {

    private final Game main;
    public WindowHandler(Game main){
        this.main = main;
        this.main.frame.addWindowListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        if(this.main.player != null){
        Packet01Disconnect packet = new Packet01Disconnect(this.main.player.getUsername());
        packet.writeData(this.main.socketClient);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
