package com.earthsway.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public InputHandler(Main main){
        main.addKeyListener(this);
    }

    public class Key{
        private int timesPressed = 0;
        private boolean pressed = false;

        public int getTimesPressed() {
            return timesPressed;
        }

        public boolean isPressed(){
            return pressed;
        }
        public void toggle(boolean isPressed){
            pressed = isPressed;
            if(isPressed) timesPressed++;
        }
    }

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();

    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    public void keyTyped(KeyEvent e){

    }

    public void toggleKey(int keyCode, boolean isPressed){
        if (keyCode == KeyEvent.VK_W || keyCode ==KeyEvent.VK_UP)up.toggle(isPressed);
        if (keyCode == KeyEvent.VK_S || keyCode ==KeyEvent.VK_DOWN)down.toggle(isPressed);
        if (keyCode == KeyEvent.VK_A || keyCode ==KeyEvent.VK_LEFT)left.toggle(isPressed);
        if (keyCode == KeyEvent.VK_D || keyCode ==KeyEvent.VK_RIGHT)right.toggle(isPressed);
    }
}