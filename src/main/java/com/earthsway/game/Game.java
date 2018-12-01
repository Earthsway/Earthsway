package com.earthsway.game;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.earthsway.game.utilities.DiscordData;
import com.earthsway.game.utilities.Sound;
import com.earthsway.game.utilities.SoundType;

import javax.swing.*;
import java.awt.*;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 400;//400
    public static final int HEIGHT = WIDTH/17*9;
    public static final int SCALE = 4;//4
    public static final String NAME = "Earthsway";
    public static Game main;
    public static final Dimension DIMENSIONS = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);


    private Thread thread;

    public JFrame frame;
    public boolean running = false;
    public int tickCount = 0;

    public boolean debugMode = true;

    public static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static DiscordData discordUser = null;
    public static DiscordRichPresence presence = new DiscordRichPresence();
    private static DiscordRichPresence lastSentPresence = null;

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

        main.start();
    }

    public void init(){
        main = this;
    }

    private void initSounds() {
        for (SoundType t : SoundType.values()) {
            if (t.getSounds() != null && t.getSounds().length > 0 ) {
                for(int i = 0; i < t.getSounds().length; i++) {
                    if(t.getAsResource(i) != null && t.getSounds()[i] != null && t.getSounds()[i] != "") {
                        new Sound(t, i, 0.65f, t.shouldLoop());
                        new Sound(t, i, 0.01f, t.shouldLoop());
                    }
                }
            }
        }
    }

    public synchronized void start() {

    }

    public synchronized void stop(){
        discordRPC.Discord_Shutdown();
        running = false;
    }

    public void run() {

        init();

       /* while(running){
                debug(DebugLevel.INFO, "{" + frames + " frames, " + ticks + " Ticks} Biome: " + player.getBiome());
        }*/
    }

    public void runDiscordRPC() {
        String applicationId = "472228275586990090";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> {//System.out.println("Discord Ready " + user.username + "!");
            discordUser = new DiscordData(user.userId, user.username, user.discriminator, user.avatar);
        };
        discordRPC.Discord_Initialize(applicationId, handlers, true, steamId);

        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Messing Around";
        discordRPC.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                discordRPC.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler").start();

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if(Game.lastSentPresence != presence) Game.discordRPC.Discord_UpdatePresence(Game.presence);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Update-Presence-Handler").start();

        while (discordUser == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("Hello: " + discordUser.username() + "#" + discordUser.discriminator());
    }

    public void tick(){

    }

    private Graphics g;

    public void render() {

    }

    public void debug(DebugLevel level, String msg){
        switch (level){
            default:
            case INFO:
                if(debugMode){
                    System.out.println("[" + NAME + "][INFO] " + msg);
                    break;
                }
            case WARNING:
                if(debugMode){
                    System.out.println("[" + NAME + "][WARNING] " + msg);
                    break;
                }
            case SEVERE:
                if(debugMode){
                    System.out.println("[" + NAME + "][SEVERE] " + msg);
                    this.stop();
                    break;
                }

        }
    }

    private Thread toggleDebugThread = null;
    public void toggleDebug() {
        if(toggleDebugThread != null && toggleDebugThread.isAlive()){
            toggleDebugThread.interrupt();
            toggleDebugThread = null;
        }
        toggleDebugThread = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {}
            debugMode = !debugMode;
        }, "toggleDebug");
        toggleDebugThread.start();
    }

    public static enum DebugLevel{
        INFO,
        WARNING,
        SEVERE
    }
}
