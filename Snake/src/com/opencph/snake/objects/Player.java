package com.opencph.snake.objects;

import java.util.ArrayList;

import com.opencph.engine.GameContainer;
import com.opencph.engine.Renderer;
import com.opencph.engine.gfx.Color;
import com.opencph.engine.vector.Vector;
import com.opencph.snake.GameObject;

public class Player extends GameObject {

    private Color c;
    private int speedX, speedY;
    private ArrayList<Vector> body;
    private boolean dead = false;
    private int len;

    public Player(int x, int y, int w, int h, String name) {
        this.setPosX(x);
        this.setPosY(y);
        this.setHeight(h);
        this.setWidth(w);
        this.setName(name);
        speedX = 0;
        speedY = 0;
        c = new Color(255, 255, 0);
        len = 0;
        
        body = new ArrayList<>();
        body.add(new Vector(this.getPosX(), this.getPosY()));
    }

    public void setDirection(int x, int y) {
        speedX = x;
        speedY = y;
    }

    public void grow() {
        Vector head = this.body.get(this.body.size() - 1).copy();
        len++;
        this.body.add(head);
    }

    public boolean eat(Vector food) {
        Vector head = this.body.get(this.body.size() - 1);
        if ((int)head.x == food.x && (int)head.y == food.y) {
            this.grow();
            return true;
        }
        return false;
    }

    public boolean endGame() {
        Vector head = this.body.get(this.body.size() - 1);
        if (head.x > 50 - 1 || head.x < 0 || head.y > 50 - 1 || head.y < 0) return true;
        
        for (int i = 0; i < this.body.size() - 1; i++) {
            Vector bPart = this.body.get(i);
            if (bPart.x == head.x && bPart.y == head.y) return true;
        }

        return false;
    }

    @Override
    public void update(GameContainer gc, double dt) {
        Vector head = this.body.get(this.body.size() - 1).copy();
        this.body.remove(0);
        head.x += (dt) * speedX;
        head.y += (dt) * speedY;
        this.body.add(head);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if (!this.isDead()) this.update(gc, 1.0);
        for (int i = 0; i < this.body.size(); i++) {
            Vector v = this.body.get(i);
            r.drawFillRect((int)v.x, (int)v.y, this.getWidth(), this.getHeight(), c);
        }
    }

    public void reset() {
        body = new ArrayList<>();
        body.add(new Vector(this.getPosX(), this.getPosY()));
        this.dead = false;
        len = 0;
        this.setDirection(0, 0);
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getLen() {
        return this.len;
    }
    
}