package com.opencph.engine.gfx;

public class Light {

    public static final int NONE = 0;
    public static final int FULL = 1;

    private int radius, diameter, color;
    private int[] lightMap;

    public Light(int radius, int color) {
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.color = color;
        this.lightMap = new int[diameter * diameter];

        for (int y = 0; y < diameter; y++ ) {
            for (int x = 0; x < diameter; x++) {
                int xOff = x - this.radius;
                int yOff = y - this.radius;
                double dist = Math.sqrt(xOff * xOff + yOff * yOff);

                if (dist < radius) {
                    double power = 1 - (dist / this.radius);
                    this.lightMap[x + y * diameter] = ((int)(((color >> 16) & 0xff) * power) << 16 | 
                                                    (int)(((color >> 8) & 0xff) * power) << 8 |
                                                    (int)((color & 0xff) * power));
                } else {
                    this.lightMap[x + y * diameter] = 0;
                }
            }
        }
    }

    public int getLightValue(int x, int y) {
        if (x < 0 || x >= diameter || y < 0 ||y >= diameter) return 0;

        return lightMap[x + y * diameter]; 
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDiameter() {
        return this.diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getLightMap() {
        return this.lightMap;
    }

    public void setLightMap(int[] lightMap) {
        this.lightMap = lightMap;
    }

}