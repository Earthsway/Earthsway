package com.earthsway.game.utilities;

public class Coords {
    protected int x;
    protected int y;

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public Coords(int x, int y){
        this.x = x;
        this.y = y;
        //System.out.println("{" + x + ", " + y + "}");
    }
}
