package com.opencph.engine.gfx;

public class ImageRequest {
    private Image image;
    private int zDepth;
    private int offX, offY;
    
    public ImageRequest(Image image, int zDepth, int offX, int offY) {
        this.image = image;
        this.zDepth = zDepth;
        this.offX = offX;
        this.offY = offY;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getZDepth() {
        return this.zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public int getOffX() {
        return this.offX;
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public int getOffY() {
        return this.offY;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }

}