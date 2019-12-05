package com.opencph.engine.gfx;

public class LightRequest {
    private Light light;
    private int locX, locY;

    public LightRequest(Light l, int x, int y) {
        this.light = l;
        this.locX = x;
        this.locY = y;
    }

    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public int getLocX() {
        return this.locX;
    }

    public void setLocX(int locX) {
        this.locX = locX;
    }

    public int getLocY() {
        return this.locY;
    }

    public void setLocY(int locY) {
        this.locY = locY;
    }

}