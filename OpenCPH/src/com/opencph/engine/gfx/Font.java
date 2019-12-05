package com.opencph.engine.gfx;

public class Font {

    public static final Font STANDARD = new Font("/fonts/font.png");
    
    private Image fontImage;
    private int[] offsets;
    private int[] widths;
    
    public Font(String path) {
        fontImage = new Image(path);

        offsets = new int[59];
        widths = new int[59];

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {
            if (fontImage.getPixels()[i] == 0xff0000ff) {
                offsets[unicode] = i;
            }

            if (fontImage.getPixels()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return this.fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return this.offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return this.widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }

}