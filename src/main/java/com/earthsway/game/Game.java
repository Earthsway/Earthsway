package com.earthsway.game;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.earthsway.Utilities;
import com.earthsway.game.entities.Player;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.entities.Worker;
import com.earthsway.game.gfx.Assets;
import com.earthsway.game.gfx.GameCamera;
import com.earthsway.game.utilities.*;
import com.earthsway.game.gfx.Hud;
import com.earthsway.game.gfx.SpriteSheet;
import com.earthsway.game.level.Level;
import com.earthsway.game.net.GameClient;
import com.earthsway.game.net.GameServer;
import com.earthsway.game.net.packets.Packet00Login;
import com.earthsway.game.utilities.Managers.KeyManager;
import com.earthsway.game.utilities.Managers.MouseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

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

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int pixels[] = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6*6*6];

    public InputHandler input;
    public WindowHandler windowHandler;
    public Level level;
    public Player player;

    public GameClient socketClient;
    public GameServer socketServer;

    public boolean debugMode = false;

    public static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static DiscordData discordUser = null;
    public static DiscordRichPresence presence = new DiscordRichPresence();
    private static DiscordRichPresence lastSentPresence = null;

    //States
    public State gameState;
    public State menuState;

    //Input
    private KeyManager keyManager;
    private MouseManager mouseManager;

    //Camera
    private GameCamera gameCamera;

    //Handler
    private Handler handler;

    public void init(){
        main = this;
        /*int index = 0;
        for(int r = 0; r<6;r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r*255/5);
                    int gg = (g*255/5);
                    int bb = (b*255/5);

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }*/
        Assets.init(new SpriteSheet("/sprite_sheet.png", 16));
       // screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png", 16));
        input = new InputHandler(this);
        level = new Level("/levels/small_test_level.png");

        if(!loadRecommendedSettings && JOptionPane.showConfirmDialog(this, "Do you want to use your discord?\nThis will only work if discord is running.\n(RECOMMENDED YES)") == 0){runDiscordRPC();
        if(JOptionPane.showConfirmDialog(this, "Do you want to use your discord name?\n(RECOMMENDED NO)") == 0) {
            player = new PlayerMP(level, 100, 100, input, discordUser.username(), null, -1);}
        else player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please Enter A Username.\n(RECOMMENDED CANCEL)"), null, -1);}
        else if (!loadRecommendedSettings)player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please Enter A Username.\n(RECOMMENDED CANCEL)"), null, -1);
        initSounds();
        if(loadRecommendedSettings){
           // runDiscordRPC();
            player = new PlayerMP(level, 100, 100, input, "", null, -1);
        }
        level.addEntity(player);
        Worker worker = new Worker(level, 90, 90, true);
        level.addEntity(worker);
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
        if(socketServer != null){
            socketServer.addConnection((PlayerMP) player, loginPacket);
        }
        //socketClient.sendData("ping".getBytes());
        loginPacket.writeData(socketClient);
        new Sound(SoundType.MAIN_MENU, 0f);

        handler = new Handler(this);
        gameCamera = new GameCamera(handler, 0, 0);

        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        State.setState(menuState);

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

    private boolean loadRecommendedSettings = false;
    public synchronized void start() {
        keyManager = new KeyManager();
        mouseManager = new MouseManager();
        running = true;
        thread = new Thread(this, NAME + "_main");
        thread.start();

        new Sound(SoundType.MAIN_MENU, 0.65f);

        loadRecommendedSettings = JOptionPane.showConfirmDialog(this, "Do you want to load Recommended Settings?\n(RECOMMENDED YES)") == 0;

        if (!loadRecommendedSettings && JOptionPane.showConfirmDialog(this, "Do you want to run the server?\n(RECOMMENDED NO)") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "173.73.91.159");
        socketClient.start();
    }

    public synchronized void stop(){
        Sound.stopAllSounds();
        discordRPC.Discord_Shutdown();
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double d = 0;

        init();

        while(running){
            long now = System.nanoTime();
            d += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (d >= 1){
                ticks++;
                tick();
                d -= 1;
                shouldRender = true;
            }

            /*try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            if(shouldRender) {
                frames++;
                render();
            }

            if(System.currentTimeMillis() - lastTimer >= 1000){
                lastTimer += 1000;
                //debug(DebugLevel.INFO,"{" + frames + " frames, " + ticks + " Ticks}");
                debug(DebugLevel.INFO, "{" + frames + " frames, " + ticks + " Ticks} Biome: " + player.getBiome());
                frames = 0;
                ticks = 0;
            }
        }
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
        tickCount++;
        level.tick();
        keyManager.tick();
        if(State.getState() != null)
            State.getState().tick();
    }

    public Graphics g;

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(4);//The Higher, the more power needed! def: 3
            return;
        }
        g = bs.getDrawGraphics();
        //int xOffset = player.x - (screen.width/2);
        //int yOffset = player.y - (screen.height/2);

        level.renderTiles();

        /*for(int x=0; x<level.width;x++){
            int color = Colors.get(-1,-1,-1,000);
            if(x % 10 == 0 && x != 0){
                color = Colors.get(-1,-1,-1,500);
            }
            Font.render((x%10)+"", screen, (x*8),0, color);
        }*/

        level.renderEntities();

        //Font.render("COLOR", screen, screen.xOffset + screen.width/2,screen.yOffset + screen.height/2,Colors.get(-1, -1, -1, 000), 1,  true);

        /*for(int y = 0; y<screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colorCode = screen.pixels[x+y * screen.width];
                if(colorCode < 255) pixels[x+y*WIDTH] = colors[colorCode];
            }
        }*/

        //graphics.drawRect(0,0,getWidth(),getHeight());
        //graphics.drawImage(image, 0,0, getWidth(), getHeight(), null);

        new Hud(g);

        g.clearRect(0, 0, width, height);
        if(State.getState() != null)
            State.getState().render(g);

        bs.show();
        g.dispose();
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
    public KeyManager getKeyManager(){
        return keyManager;
    }

    public MouseManager getMouseManager(){
        return mouseManager;
    }

    public GameCamera getGameCamera(){
        return gameCamera;
    }
}
