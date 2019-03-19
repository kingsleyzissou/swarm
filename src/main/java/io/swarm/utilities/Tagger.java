package io.swarm.utilities;

import io.swarm.collections.CoordinateSet;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import io.swarm.collections.DisjointSet;

import java.util.ArrayList;

public class Tagger {

    private Pane pane;
    private double imageWidth;
    private DisjointSet set;
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

    public Tagger(DisjointSet set, double imageWidth, Pane pane) {
        this.set = set;
        this.imageWidth = imageWidth;
        this.pane = pane;
    }

    /**
     * Begin preparing the image for tagging
     * Calls the methods to set the boundaries of the tag,
     * creates the tag box and then passes the rectangles
     * to the next method
     *
     */
    public void tagImage() {
        int count = 1;
        for(int rootIndex : this.set.getRootIndices()) {
            int clusterSize = getClusterWeight(rootIndex);
            if(clusterSize != 0) {
                int[] birdBounds = getCoordinates(rootIndex);
                setClusterBoundaries(rootIndex, birdBounds);
                String clusterNumber = getClusterNumber(count, clusterSize);
                count = count + clusterSize;
                rectangles.add(buildRectangle(birdBounds, clusterNumber));
            }
        }
        addRectanglesToPane(rectangles);
    }

    /**
     * Calculate the weight of each cluster
     *
     * @param index of the cluster being analysed
     * @return weight of the cluster
     */
    private int getClusterWeight(int index) {
        int size = set.clusterSize(set.getValue(index));
        return set.getStatistics().weight(size);
    }

    /**
     * Set the boundaries of the cluster tag
     *
     * @param rootIndex the index of the root of the set
     * @param birdBounds the boundaries to be updated
     */
    private void setClusterBoundaries(int rootIndex, int[] birdBounds) {
        for(int i = 0; i < set.getSize(); i++) {
            if(set.getValue(i) == rootIndex) {
                updateBounds(i, birdBounds);
            }
        }
    }


    /**
     * Gather the coordinates
     *
     * @param index from which the coordinates are gathered
     * @return array of 4 coordinates
     */
    private int[] getCoordinates(int index) {
        return new int[]{
            CoordinateSet.getXCoordinate(index, (int) imageWidth),
            CoordinateSet.getXCoordinate(index, (int) imageWidth),
            CoordinateSet.getYCoordinate(index, (int) imageWidth),
            CoordinateSet.getYCoordinate(index, (int) imageWidth)
        };
    }

    /**
     * Update the bounds of the tag
     *
     * @param index element of tag to be updated
     * @param bounds the boundaries of the tag
     */
    private void updateBounds(int index, int[] bounds) {
        int x = CoordinateSet.getXCoordinate(index, (int) imageWidth);
        int y = CoordinateSet.getYCoordinate(index, (int) imageWidth);
        bounds[0] = (x < bounds[0]) ? x : bounds[0];
        bounds[1] = (x > bounds[1]) ? x : bounds[1];
        bounds[2] = (y < bounds[2]) ? y : bounds[2];
        bounds[3] = (y > bounds[3]) ? y : bounds[3];
    }

    /**
     * Pad the tag box
     *
     * @param bounds the bounds to be padded
     */
    private void addPadding(int[] bounds) {
        bounds[0] = (bounds[0] - 5 > 0) ? bounds[0] - 5 : bounds[0];
        bounds[1] = (bounds[1] + 5 < set.getSize()) ? bounds[1] + 5 : bounds[1];
        bounds[2] = (bounds[2] - 5 > 0) ? bounds[2] - 5 : bounds[2];
        bounds[3] = (bounds[3] + 5 < set.getSize()) ? bounds[3] + 5 : bounds[3];
    }

    /**
     * Install the rectangles to the
     *
     * @param rectangles to be installed to the pane
     */
    private void addRectanglesToPane(ArrayList<Rectangle> rectangles) {
        pane.getChildren().addAll(rectangles);
    }

    /**
     * Construct the rectangle to be added to the scene
     *
     * @param bounds of the rectangle
     * @param clusterNumber label for the rectangle
     * @return rectangle with tooltip label
     */
    private Rectangle buildRectangle(int[] bounds, String clusterNumber) {
        addPadding(bounds);
        Rectangle r = new Rectangle(bounds[0], bounds[2], bounds[1] - bounds[0] , bounds[3] - bounds[2]);
        r.setStroke(Paint.valueOf("#00b9d8"));
        r.setFill(Color.TRANSPARENT);
        Tooltip t = new Tooltip(clusterNumber);
        Tooltip.install(r, t);
        return r;
    }

    /**
     * Calculate the number of the cluster based on the weight of the cluster.
     * The number is used for the label of the tag
     *
     * @param count the current count
     * @param clusterSize weighted size of the cluster
     * @return the cluster number in a string to be used for a label
     */
    private String getClusterNumber(int count, int clusterSize) {
        StringBuilder str = new StringBuilder();
        if(clusterSize == 1) return str.append("Bird: ").append(count).toString();
        str.append("Birds: ");
        for(int i = count; i < count + clusterSize; i++) {
            if(i != (count + clusterSize) - 1)
                str.append(i).append(", ");
            else
                str.append(i);
        }
        return str.toString();
    }

}

