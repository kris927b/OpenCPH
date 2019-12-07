package com.opencph.engine;

public abstract class AbstractGame {

    public abstract void init(GameContainer gc);
    public abstract void update(GameContainer gc, double dt);
    public abstract void render(GameContainer gc, Renderer r);
}