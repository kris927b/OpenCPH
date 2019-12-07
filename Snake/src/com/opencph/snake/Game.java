package com.opencph.snake;

import java.awt.event.KeyEvent;

import com.opencph.engine.AbstractGame;
import com.opencph.engine.GameContainer;
import com.opencph.engine.Renderer;
import com.opencph.engine.gfx.Color;
import com.opencph.engine.vector.Vector;
import com.opencph.snake.objects.Player;

public class Game extends AbstractGame {
    Player p;
    Vector food;
    boolean dead;

    public Game() {
        p = new Player(10, 10, 1, 1, "Snake");
        food = createFood();
    }

    public Vector createFood() {
        int x = (int) Math.floor(Math.random() * 49);
        int y = (int) Math.floor(Math.random() * 49);
        return new Vector(x, y);
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(Color.WHITE);
    }

    @Override
    public void update(GameContainer gc, double dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_UP)) {
            p.setDirection(0, -1);
        } else if (gc.getInput().isKeyDown(KeyEvent.VK_DOWN)) {
            p.setDirection(0, 1);
        } else if (gc.getInput().isKeyDown(KeyEvent.VK_LEFT)) {
            p.setDirection(-1, 0);
        } else if (gc.getInput().isKeyDown(KeyEvent.VK_RIGHT)) {
            p.setDirection(1, 0);
        } else if (gc.getInput().isKeyDown(KeyEvent.VK_ESCAPE)) {
            // p.setDirection(0, 0);
            p.setDead(!p.isDead());
        } else if (dead && gc.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
             p.reset();
             dead = false;
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if (p.eat(food)) food = createFood();
        r.drawFillRect(0, 0, 50, 50, new Color(23, 23, 23));
        p.render(gc, r);
        r.drawFillRect((int)food.x, (int)food.y, 1, 1, Color.WHITE);
        if (p.endGame()) {
            dead = true;
            p.setDead(dead);
        }
        if (dead) {
            r.drawText("END GAME", 7, 20, Color.RED);
        } else {
            r.drawText("" + p.getLen(), 0, 0, Color.GREEN);
        }
    }

    public static void main(String args[]) {
        GameContainer gc = new GameContainer(new Game());
        gc.setHeight(50);
        gc.setWidth(50);
        gc.setScale(20);
        gc.start();
    }
}