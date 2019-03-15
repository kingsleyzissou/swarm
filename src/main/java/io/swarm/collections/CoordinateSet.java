package io.swarm.collections;

import java.util.HashSet;
import java.util.Iterator;

public class CoordinateSet implements Iterable{

    private HashSet<double[]> coordinates;

    public CoordinateSet () {
        coordinates = new HashSet<>();
    }

    /**
     * Add coordinates to the set
     *
     * @param xy coordinates to be added
     */
    public void add(double[] xy) {
        coordinates.add(xy);
    }

    /**
     * Get the size of the set
     *
     * @return size of the set
     */
    public int size() {
        return coordinates.size();
    }

    /**
     * Convert the set to a 2D array, for use in linear regression
     *
     * @return 2D array of coordinates
     */
    public double[][] to2DArray() {
        double[][] doubles = new double[coordinates.size()][];
        return coordinates.toArray(doubles);
    }

    /**
     * Helper method to convert index of 1D array to an x coordinate
     *
     * @param index element to be converted
     * @param width of the plain
     * @return x coordinate of the image
     */
    public static int getXCoordinate(int index, int width) {
        return index % width;
    }


    /**
     * Helper method to convert index of 1D array to an y coordinate
     *
     * @param index element to be converted
     * @param width of the plain
     * @return y coordinate of the image
     */
    public static int getYCoordinate(int index, int width) {
        return  index / width;
    }

    /**
     * Iterator in order to cycle through elements
     * @return parent iterator
     */
    @Override
    public Iterator iterator() {
        return coordinates.iterator();
    }

}
