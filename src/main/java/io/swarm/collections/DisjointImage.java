package io.swarm.collections;

import io.swarm.utilities.ImageInfo;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class DisjointImage {

    private double height;
    private double width;
    private Image original;
    private DisjointSet set;
    private ImageInfo info;
    private WritableImage gray;
    private ArrayList<Color> colours = new ArrayList<>();

    public DisjointImage(Image original, DisjointSet set, ImageInfo info) {
        this.height = original.getHeight();
        this.width = original.getWidth();
        this.original = original;
        this.set = set;
        this.info = info;
        this.gray = new WritableImage((int) width, (int) height);

        Collections.addAll(colours,
                Color.BLUE, Color.RED, Color.PALEGREEN, Color.VIOLET,
                Color.FIREBRICK, Color.TURQUOISE, Color.TOMATO,
                Color.YELLOW,  Color.INDIGO, Color.LIME,
                Color.RED, Color.TEAL, Color.BEIGE, Color.AZURE,
                Color.HONEYDEW, Color.CHOCOLATE, Color.CORAL,
                Color.THISTLE, Color.TAN, Color.ORANGERED, Color.ORCHID,
                Color.SEAGREEN, Color.SEASHELL
        );
    }

    /**
     * Convert individual pixel of an image to either black or white
     *
     * @param x coordinate of the pixel being manipulated
     * @param y coordinate of the pixel being manipulated
     * @param reader PixelReader object
     * @param writer PixelWriter object
     * @param threshold the brightness threshold for difference between black and white
     * @return monochrome pixel
     */
    private Color toMonochrome(int x, int y, PixelReader reader, PixelWriter writer, double threshold) {
        Color color = reader.getColor(x, y);
        double lum = color.getBrightness() * 100;
        Color newColor = lum <= threshold ? Color.BLACK : Color.WHITE;
        writer.setColor(x,y, newColor);
        return newColor;
    }

    /**
     * Filter an image to black and white
     *
     * @param threshold point used to differentiate limit of white and black
     * @return monochrome image
     */
    public WritableImage filter(double threshold) {
        PixelReader reader = original.getPixelReader();
        PixelWriter writer = gray.getPixelWriter();
        for(int y=0; y < height ; y++) {
            for(int x = 0; x < width; x++) {
                Color color = toMonochrome(x, y, reader, writer, threshold);
                initialiseSets(set, x, y, (int) width, color);
            }
        }
        return gray;
    }

    /**
     * Overloaded method for filtering an image with default threshold
     *
     * @return filtered image
     */
    public WritableImage filter() {
        return filter(60);
    }

    /**
     * Initialise the disjoint set based on the colour of the individual pixel
     *
     * @param set disjoint set being initialised
     * @param x coordinate of the pixel
     * @param y coordinate of the pixel
     * @param width width of the image
     * @param color monochrome colour of the pixel
     */
    public void initialiseSets(DisjointSet set, int x, int y, int width, Color color) {
        double lum = color.getBrightness() * 100;
        int value= (lum <= 50) ? set.getRootValue(1) : -1;
        set.setValue(x + width * y, value);
    }

    /**
     * Get the width of the image being manipulated
     *
     * @return width of the image
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Get the height of the image being manipulated
     *
     * @return height of the image
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Return the original, unaffected image
     *
     * @return original image
     */
    public Image getOriginalImage() {
        return original;
    }

    public WritableImage recolorImage() {
        WritableImage rainbow = new WritableImage((int) original.getWidth(), (int) original.getHeight());
        PixelWriter wr = rainbow.getPixelWriter();

        for (int i = 0; i < set.getSize() - 1; i++) {
            if(set.getValue(i) != -1) {
                int index = set.find(i);
                int mod =  index % colours.size();
                Color c = colours.get(mod);
                int x = CoordinateSet.getXCoordinate(i, (int) original.getWidth());
                int y = CoordinateSet.getYCoordinate(i, (int) original.getWidth());
                wr.setColor(x,y,c);
            }
        }

        return rainbow;
    }

    /**
     * Get the underlying disjoint set
     *
     * @return the disjoint set corresponding to the image being manipulated
     */
    public DisjointSet getSet() {
        return set;
    }

    /**
     * Getter method for the Image Info object
     *
     * @return image info object
     */
    public ImageInfo getInfo() {
        return info;
    }
}
