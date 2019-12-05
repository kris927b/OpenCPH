package com.opencph.game;

import com.opencph.engine.AbstractGame;
import com.opencph.engine.GameContainer;
import com.opencph.engine.Renderer;
import com.opencph.engine.gfx.Image;
import com.opencph.engine.gfx.Light;

public class GameManager extends AbstractGame {

    private Light light;
    private Image image;
    private Image image2;

    public GameManager() {
        image = new Image("/test2.png");
        image2 = new Image("/test.png");
        image2.setLightBlock(Light.FULL);
        light = new Light(50, 0xff00ffff);
    }

    @Override
    public void update(GameContainer gc, double dt) {
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        r.setZDepth(0);
        r.drawImage(image, 0,0);
        r.drawImage(image2, 100, 100);
    }
    
    public static void main(String args[]) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}