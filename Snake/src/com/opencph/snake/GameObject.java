package com.opencph.snake;

import com.opencph.engine.GameContainer;
import com.opencph.engine.Renderer;

public abstract class GameObject {

    private double posX, posY;
    private int width, height;

    private String name;

    private boolean removed = false;

    public abstract void update(GameContainer gc, double dt);
    public abstract void render(GameContainer gc, Renderer r);

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "{" +
            " posX='" + getPosX() + "'" +
            ", posY='" + getPosY() + "'" +
            ", width='" + getWidth() + "'" +
            ", height='" + getHeight() + "'" +
            ", name='" + getName() + "'" +
            ", removed='" + isRemoved() + "'" +
            "}";
    }

}