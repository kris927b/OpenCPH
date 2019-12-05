package com.opencph.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.opencph.engine.gfx.Font;
import com.opencph.engine.gfx.Image;
import com.opencph.engine.gfx.ImageTile;
import com.opencph.engine.gfx.Light;
import com.opencph.engine.gfx.LightRequest;
import com.opencph.engine.gfx.ImageRequest;

class ReturnObj {
    int newX, newY, newHeight, newWidth;
    ReturnObj(int x, int y, int height, int width) {
        newX = x;
        newY = y;
        newHeight = height;
        newWidth = width;
    }
}

public class Renderer {

    private int pW, pH;
    private int[] p;
    private int[] zBuffer;
    private int[] lightMap;
    private int[] lightBlock;

    private int ambientColor = 0xff232323;
    private int zDepth = 0;
    private boolean processing = false;

    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
    private ArrayList<LightRequest> lightRequests = new ArrayList<LightRequest>();

    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
        lightMap = new int[p.length];
        lightBlock = new int[p.length];
    }

    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }

    public void process() {
        setProcessing(true);

        Collections.sort(imageRequest, new Comparator<ImageRequest>() {
            @Override
            public int compare(ImageRequest o1, ImageRequest o2) {
                if (o1.getZDepth() < o2.getZDepth()) return -1;
                if (o1.getZDepth() > o2.getZDepth()) return 1;
                return 0;
            }
        });

        for (int i = 0; i < imageRequest.size(); i++) {
            ImageRequest ir = imageRequest.get(i);
            setZDepth(ir.getZDepth());
            drawImage(ir.getImage(), ir.getOffX(), ir.getOffY());
        }

        for (int i = 0; i < lightRequests.size(); i++) {
            LightRequest lr = lightRequests.get(i);
            drawLightRequest(lr.getLight(), lr.getLocX(), lr.getLocY());
        }

        for (int i = 0; i < lightMap.length; i++) {
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;

            p[i] = ((int)(((p[i] >> 16) & 0xff) * r) << 16 | 
                    (int)(((p[i] >> 8) & 0xff) * g) << 8 |
                    (int)((p[i] & 0xff) * b));
        }

        imageRequest.clear();
        lightRequests.clear();
        setProcessing(false);
    }
    
    public void setPixel(int x, int y, int value) {
        int alpha = ((value >> 24) & 0xff);
        
        if ((x < 0 || x >= pW || y < 0 ||y >= pH) || alpha == 0) return;
        
        int index = x + y * pW;

        if (zBuffer[index] > zDepth) return;

        zBuffer[index] = zDepth;

        if (alpha == 255) p[index] = value;
        else {
            int pixelColor = p[index];
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            p[index] = (newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void setLightMap(int x, int y, int value) {
        // Off-screen?
        if (x < 0 || x >= pW || y < 0 ||y >= pH) return;

        int baseColor = lightMap[x + y * pW];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);

        lightMap[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void setLightBlock(int x, int y, int value) {
        // Off-screen?
        if (x < 0 || x >= pW || y < 0 ||y >= pH) return;

        if (zBuffer[x + y * pW] > zDepth) return;

        lightBlock[x + y * pW] = value;
    }

    public ReturnObj offScreen(int offX, int offY, int width, int height) {
        // Don't render if off screen
        if (offX < -width) return null;
        if (offY < -height) return null;
        if (offX >= pW) return null;
        if (offY >= pH) return null;
        
        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        // Clipping 
        if (offX < 0) newX -= offX;
        if (offY < 0) newY -= offY;
        if (newWidth + offX > pW) newWidth -= newWidth + offX - pW;
        if (newHeight + offY > pH) newHeight -= newHeight + offY - pH;

        return new ReturnObj(newX, newY, newHeight, newWidth);
    }

    public void drawImage(Image image, int offX, int offY) {
        if (image.isAlpha() && !isProcessing()){
            imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }
        
        ReturnObj sizes = offScreen(offX, offY, image.getWidth(), image.getHeight());
        if (sizes == null) return;

        for (int y = sizes.newY; y < sizes.newHeight; y++) {
            for (int x = sizes.newX; x < sizes.newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
                setLightBlock(x + offX, y + offY, image.getLightBlock());
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
        if (image.isAlpha() && !isProcessing()){
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }
        
        ReturnObj sizes = offScreen(offX, offY, image.getTileW(), image.getTileH());
        if (sizes == null) return;

        for (int y = sizes.newY; y < sizes.newHeight; y++) {
            for (int x = sizes.newX; x < sizes.newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
                setLightBlock(x + offX, y + offY, image.getLightBlock());
            }
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        Image fontImage = font.getFontImage();
        
        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < fontImage.getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    if (fontImage.getPixels()[(x + font.getOffsets()[unicode]) + y * fontImage.getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {
        ReturnObj sizes = offScreen(offX, offY, width, height);
        if (sizes == null) return;
        
        for (int y = sizes.newY; y < sizes.newHeight; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for (int x = sizes.newX; x < sizes.newWidth; x++) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    public void drawFillRect(int offX, int offY, int width, int height, int color) {
        ReturnObj sizes = offScreen(offX, offY, width, height);
        if (sizes == null) return;
        
        for (int y = sizes.newY; y < sizes.newHeight; y++) {
            for (int x = sizes.newX; x < sizes.newWidth; x++) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, int color) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        int D = (2 * dy) - dx;
        int y = y0;

        for (int x = x0; x < x1; x++) {
            setPixel(x, y, color);
            if (D > 0) {
                y += 1;
                D = D - 2 * dx;
            }
            D = D + 2 * dy;
        }
    }

    public void drawEllipse(int centerX, int centerY, int width, int height, int color) {
        
    }

    public void drawLight(Light l, int offX, int offY) {
        lightRequests.add(new LightRequest(l, offX, offY));
    }

    private void drawLightRequest(Light l, int offX, int offY) {
        for (int i = 0; i <= l.getDiameter(); i++) {
            drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY);
            drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY);
        }
    }

    private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;
        int err2;

        while(true) {
            int screenX = x0 - l.getRadius() + offX;
            int screenY = y0 - l.getRadius() + offY;

            if (screenX < 0 || screenX >= pW || screenY < 0 ||screenY >= pH) return;

            int lightColor = l.getLightValue(x0, y0);
            if (lightColor == 0) return;

            if (lightBlock[screenX + screenY * pW] == Light.FULL) return;

            setLightMap(screenX, screenY, lightColor);

            if (x0 == x1 && y0 == y1)
                break;

            err2 = 2 * err;

            if (err2 > -1 * dy) {
                err -= dy;
                x0 += sx;
            }

            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    public int getZDepth() {
        return this.zDepth;
    }

    public void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public boolean isProcessing() {
        return this.processing;
    }

    public boolean getProcessing() {
        return this.processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

}