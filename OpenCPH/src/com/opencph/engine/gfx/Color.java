package com.opencph.engine.gfx;

public class Color {
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(255, 255, 255);

    int alpha, red, green, blue;

    public Color(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int red, int green, int blue) {
        this.alpha = 255;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getColorCode() {
        return (alpha << 24 | red << 16 | green << 8 | blue);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

}