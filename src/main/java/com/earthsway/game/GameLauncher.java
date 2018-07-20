package com.earthsway.game;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

@SuppressWarnings("serial")
public class GameLauncher extends Applet {
    private static Main main = new Main();
    public static final boolean DEBUG_MODE = false;

    @Override
    public void init(){
        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
        setMaximumSize(Main.DIMENSIONS);
        setMinimumSize(Main.DIMENSIONS);
        setPreferredSize(Main.DIMENSIONS);
        main.debugMode = DEBUG_MODE;
    }

    @Override
    public void start(){
        main.start();
    }

    @Override
    public void stop(){
        main.stop();
    }

    public static void main(String[] args){
        main.setMinimumSize(main.DIMENSIONS);
        main.setMaximumSize(main.DIMENSIONS);
        main.setPreferredSize(main.DIMENSIONS);

        main.frame = new JFrame(main.NAME);

        main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.frame.setLayout(new BorderLayout());

        main.frame.add(main, BorderLayout.CENTER);
        main.frame.pack();

        main.frame.setResizable(false);
        main.frame.setLocationRelativeTo(null);
        main.frame.setVisible(true);

        main.windowHandler = new WindowHandler(main);
        main.debugMode = DEBUG_MODE;

        main.start();
    }
}
