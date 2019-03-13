package io.swarm;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class DisjointImage {

    private double height;
    private double width;
    private Image original;
    private DisjointSet set;
    private WritableImage gray;

    public DisjointImage(Image original, DisjointSet set) {
        this.height = original.getHeight();
        this.width = original.getWidth();
        this.original = original;
        this.set = set;
        this.gray = new WritableImage((int) width, (int) height);
    }

    public Color toMonochrome(int x, int y, PixelReader reader, PixelWriter writer) {
        Color color = reader.getColor(x, y);
        double lum = color.getBrightness() * 100;
        Color newColor = lum <= 50 ? Color.BLACK : Color.WHITE;
        writer.setColor(x,y, newColor);
        return newColor;
    }

    public WritableImage filter() {
        PixelReader reader = original.getPixelReader();
        PixelWriter writer = gray.getPixelWriter();

        for(int y=0; y < height ; y++) {
            for(int x = 0; x < width; x++) {
                Color color = toMonochrome(x, y, reader, writer);
                initialiseSets(set, x, y, (int) width, color);
            }
        }

        return gray;

    }

    public void initialiseSets(DisjointSet set, int x, int y, int width, Color color) {
        double lum = color.getBrightness() * 100;
        int value= (lum <= 50) ? set.getRootValue(1) : -1;
        set.setValue(x + width * y, value);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Image getOriginalImage() {
        return original;
    }

}
