package com.earthsway.game;

import com.earthsway.game.gfx.Screen;
import com.earthsway.game.gfx.SpriteSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Main extends Canvas implements Runnable{

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH/21*9;
    public static final int SCALE = 10;
    public static final String NAME = "Earthsway";

    private JFrame frame;
    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int pixels[] = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colours = new int[6*6*6];

    private Screen screen;
    public InputHandler input;

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

                    colours[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);
    }

    private synchronized void start(){
        running = true;
        new Thread(this).start();
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

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(shouldRender) {
                frames++;
                render();
            }

            if(System.currentTimeMillis() - lastTimer >= 1000){
                lastTimer += 1000;
                System.out.println("{" + frames + " frames, " + ticks + " Ticks}");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick(){
        tickCount++;

        if(input.up.isPressed()) screen.yOffset--;
        if(input.down.isPressed()) screen.yOffset++;
        if(input.left.isPressed()) screen.xOffset--;
        if(input.right.isPressed()) screen.xOffset++;
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);//The Higher, the more power needed!
            return;
        }

        screen.render(pixels, 0, WIDTH);

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
