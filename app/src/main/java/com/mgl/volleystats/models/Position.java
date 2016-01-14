package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 1/7/16.
 */
public class Position {
    private int x;
    private int y;
    private int player;

    public Position(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
