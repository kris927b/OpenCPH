package com.opencph.engine.gfx;

public class Light {

    public static final Color NONE = new Color(0, 0, 0);
    public static final Color FULL = new Color(255, 255, 255);

    private int radius, diameter;
    private Color color;
    private Color[] lightMap;

    public Light(int radius, Color color) {
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.color = color;
        this.lightMap = new Color[diameter * diameter];

        for (int y = 0; y < diameter; y++ ) {
            for (int x = 0; x < diameter; x++) {
                int xOff = x - this.radius;
                int yOff = y - this.radius;
                double dist = Math.sqrt(xOff * xOff + yOff * yOff);

                if (dist < radius) {
                    double power = 1 - (dist / this.radius);
                    this.lightMap[x + y * diameter] = new Color((int)(color.getRed() * power), (int)(color.getGreen() * power), (int)(color.getBlue() * power));
                } else {
                    this.lightMap[x + y * diameter] = new Color(0, 0, 0);
                }
            }
        }
    }

    public Color getLightValue(int x, int y) {
        if (x < 0 || x >= diameter || y < 0 ||y >= diameter) return NONE;

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

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color[] getLightMap() {
        return this.lightMap;
    }

    public void setLightMap(Color[] lightMap) {
        this.lightMap = lightMap;
    }

}