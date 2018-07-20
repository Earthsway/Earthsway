package com.earthsway.game;

import com.earthsway.game.entities.Player;
import com.earthsway.game.gfx.Colors;
import com.earthsway.game.gfx.Font;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.gfx.SpriteSheet;
import com.earthsway.game.level.Level;
import com.earthsway.game.net.GameClient;
import com.earthsway.game.net.GameServer;
import com.earthsway.game.net.packets.Packet00Login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Main extends Canvas implements Runnable{

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH/17*9;
    public static final int SCALE = 10;
    public static final String NAME = "Earthsway";

    private JFrame frame;
    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int pixels[] = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6*6*6];

    private Screen screen;
    public InputHandler input;
    public Level level;
    public Player player;

    private GameClient socketClient;
    private GameServer socketServer;

    public Main(){
        setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init(){
        int index = 0;
        for(int r = 0; r<6;r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r*255/5);
                    int gg = (g*255/5);
                    int bb = (b*255/5);

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);

        level = new Level("/levels/small_test_level.png");
        /*player = new Player(level, 0, 0, input, JOptionPane.showInputDialog(this, "Please Enter A Username"));
        level.addEntity(player);
        socketClient.sendData("ping".getBytes());*/
        Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog(this, "Please Enter A Username"));
        loginPacket.writeData(socketClient);
    }

    private synchronized void start(){
        running = true;
        new Thread(this).start();

        if(JOptionPane.showConfirmDialog(this, "Do you want to run the server?") == 0){
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();
    }

    private synchronized void stop(){
        running = false;
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
                frame.setTitle("{" + frames + " frames, " + ticks + " Ticks}");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick(){
        tickCount++;
        level.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(4);//The Higher, the more power needed! def: 3
            return;
        }
        int xOffset = player.x - (screen.width/2);
        int yOffset = player.y - (screen.height/2);

        level.renderTiles(screen,xOffset, yOffset);

        /*for(int x=0; x<level.width;x++){
            int color = Colors.get(-1,-1,-1,000);
            if(x % 10 == 0 && x != 0){
                color = Colors.get(-1,-1,-1,500);
            }
            Font.render((x%10)+"", screen, (x*8),0, color);
        }*/

        level.renderEntities(screen);

        //Font.render("COLOR", screen, screen.xOffset + screen.width/2,screen.yOffset + screen.height/2,Colors.get(-1, -1, -1, 000), 1,  true);

        for(int y = 0; y<screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colorCode = screen.pixels[x+y * screen.width];
                if(colorCode < 255) pixels[x+y*WIDTH] = colors[colorCode];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawRect(0,0,getWidth(),getHeight());
        g.drawImage(image, 0,0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String args[]){
        new Main().start();
    }
}
