package io.helpers;

import io.swarm.DisjointSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageHandler {

    public static Color filterImage(int x, int y, PixelReader reader, PixelWriter writer) {
        Color color = reader.getColor(x, y);
        double lum = color.getBrightness() * 100;
        Color newColor = lum <= 50 ? Color.BLACK : Color.WHITE;
        writer.setColor(x,y, newColor);
        return newColor;
    }

    public static WritableImage filter(Image original, WritableImage filtered, DisjointSet set) {
        PixelReader reader = original.getPixelReader();

        int width = (int) original.getWidth();
        int height = (int) original.getHeight();

        filtered = new WritableImage(width, height);
        PixelWriter writer = filtered.getPixelWriter();

        for(int y=0; y < height ; y++) {
            for(int x = 0; x < width; x++) {
                Color color = ImageHandler.filterImage(x, y, reader, writer);
                SetHandler.initialiseSets(set, x, y, width, color);
            }
        }

        return filtered;

    }
}
