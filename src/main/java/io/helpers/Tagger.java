package io.helpers;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import io.swarm.DisjointSet;

import java.util.ArrayList;

public class Tagger {

    private  StackPane stack;
    private  WritableImage grayscale;
    private ImageView image;
    private double imageWidth;
    private DisjointSet set;
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

    public Tagger(DisjointSet set, double imageWidth, ImageView image, WritableImage grayscale, StackPane stack) {
        this.set = set;
        this.imageWidth = imageWidth;
        this.image = image;
        this.grayscale = grayscale;
        this.stack = stack;
    }

    public void tagImage() {

        int count = 1;
        for(int rootIndex : this.set.getRootIndices()) {

            int[] birdBounds = getCoordinates(rootIndex);

            setClusterBounbaries(rootIndex, birdBounds);

            int clusterSize = set.getTreeSize(set.getValue(rootIndex));
            double average = set.getAverage();
            double diffFromAverage = set.diffFromAverage(average, clusterSize);
            clusterSize = (diffFromAverage > 0.75) ? (int) (clusterSize/average) : 1;
            String clusterNumber = getClusterNumber(count, clusterSize);
            count = count + clusterSize;


            rectangles.add(buildRectangle(birdBounds, clusterNumber));
        }

        addSquaresToWindow(rectangles);
    }

    private void setClusterBounbaries(int rootIndex, int[] birdBounds) {
        for(int i = 0; i < set.getSize(); i++) {
            if(set.getValue(i) == rootIndex) {
                updateBounds(i, birdBounds);
            }
        }
    }

    private int[] getCoordinates(int index) {
        return new int[]{
                index % ((int) imageWidth),
                index % ((int) imageWidth),
                index / ((int) imageWidth),
                index / ((int) imageWidth)
        };
    }

    private void updateBounds(int index, int[] bounds) {
        int x = index % (int) imageWidth;
        int y = index / (int) imageWidth;
        bounds[0] = (x < bounds[0]) ? x : bounds[0];
        bounds[1] = (x > bounds[1]) ? x : bounds[1];
        bounds[2] = (y < bounds[2]) ? y : bounds[2];
        bounds[3] = (y > bounds[3]) ? y : bounds[3];
    }

    private void addSquaresToWindow(ArrayList<Rectangle> rectangles) {
        Pane pane = new Pane();
        pane.getChildren().addAll(rectangles);
        Group group = new Group(image, pane);
        stack.getChildren().add(group);
        StackPane.setAlignment(group, Pos.CENTER);
    }

    private Rectangle buildRectangle(int[] bounds, String clusterNumber) {
        addPadding(bounds);
        Rectangle r = new Rectangle(bounds[0], bounds[2], bounds[1] - bounds[0] , bounds[3] - bounds[2]);
        r.setStroke(Color.RED);
        r.setFill(Color.TRANSPARENT);
        Tooltip t = new Tooltip(clusterNumber);
        Tooltip.install(r, t);
        return r;
    }

    private String getClusterNumber(int count, int clusterSize) {

        StringBuilder str = new StringBuilder();

        if(clusterSize == 1) return str.append("Bird: ").append(count).toString();

        str.append("Birds: ");

        System.out.println(count + " | " + clusterSize);
        for(int i = count; i < count + clusterSize; i++) {
            if(i != (count + clusterSize) - 1)
                str.append(i).append(", ");
            else
                str.append(i);
        }
        return str.toString();

    }

    private void addPadding(int[] bounds) {
        bounds[0] = (bounds[0] - 10 > 0) ? bounds[0] - 10 : bounds[0];
        bounds[1] = (bounds[1] + 10 < set.getSize()) ? bounds[1] + 10 : bounds[1];
        bounds[2] = (bounds[2] - 10 > 0) ? bounds[2] - 10 : bounds[2];
        bounds[3] = (bounds[3] - 10 < set.getSize()) ? bounds[3] + 10 : bounds[3];
    }
}

