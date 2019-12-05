package com.opencph.engine.gfx;

public class ImageTile extends Image {

    private int tileW, tileH;

    public ImageTile(String path, int tileW, int tileH) {
        super(path);

        this.tileW = tileW;
        this.tileH = tileH;
    }

    public Image getTileImage(int tileX, int tileY) {
        int[] p = new int[tileW * tileH];

        for (int y = 0; y < tileH ; y++) {
            for (int x = 0; x < tileW ; x++) {
                p[x + y * tileW] = getPixels()[(x + tileX * tileW) + (y + tileY * tileH) * getWidth()];
            }
        }

        return new Image(p, tileW, tileH);
    }

    public int getTileW() {
        return this.tileW;
    }

    public void setTileW(int tileW) {
        this.tileW = tileW;
    }

    public int getTileH() {
        return this.tileH;
    }

    public void setTileH(int tileH) {
        this.tileH = tileH;
    }

}