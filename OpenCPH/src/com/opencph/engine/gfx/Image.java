package com.opencph.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
    private int width, height;
    private int[] pixels;
    private boolean alpha = false;
    private Color lightBlock = Light.NONE;

    public Image(String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Image.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);

        image.flush();
    }

    public Image(int[] p, int w, int h) {
        this.pixels = p;
        this.width = w;
        this.height = h;
    }

    public Color getLightBlock() {
        return this.lightBlock;
    }

    public void setLightBlock(Color lightBlock) {
        this.lightBlock = lightBlock;
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

    public int[] getPixels() {
        return this.pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public boolean isAlpha() {
        return this.alpha;
    }

    public boolean getAlpha() {
        return this.alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

}