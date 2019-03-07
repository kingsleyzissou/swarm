package io.swarm;

import java.util.ArrayList;

public class DisjointSet {

    private int[] set;
    private int height, width;
    private Double average;
    private ArrayList<Integer> roots;

    public DisjointSet(int width, int height) {
        this.width = width;
        this.height = height;
        this.set = new int[width * height];
        roots = new ArrayList<Integer>();
    }

    public DisjointSet(double width, double height) {
        this.width = (int) width;
        this.height = (int) height;
        this.set = new int[(int) width * (int) height];
    }

    /**
     * Get the average size of a cluster
     *
     * @return average cluster size
     */
    public double getAverage() {
        if(this.average == null) calculateAverage();
        return this.average;
    }

    /**
     * Set the index of the disjoint set to a particular value
     *
     * @param index the index/key to be modified
     * @param value the value to be added
     */
    public void setValue(int index, int value) {
        this.set[index] = value;
    }

    /**
     * Checks if a node is the root element
     *
     * @param index node to be checked
     * @return true or false
     */
    public boolean isRoot(int index) {
        int value = (index > 0) ? this.set[index] : index;
        if(value == -1) return false;
        return Math.abs((value >> 31)) == 1;
    }

    /**
     * Find the root of a disjoint set node
     *
     * @param index the node for which the parent is being searched
     * @return the parent of the index node
     */
    public int find(int index) {
        return isRoot(index) ? index : find(this.set[index]);
    }

    /**
     * Quick union of two clusters, by finding the root element of each cluster
     *
     * @param a first cluster
     * @param b second cluster
     */
    public void quickUnion(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if(rootA != rootB) {
            int size = getTreeSize(this.set[rootA]) + getTreeSize(this.set[rootB]);
            this.setValue(rootB, rootA);
            this.setValue(rootA, this.getRootValue(size));
        }

    }

    /**
     * Get the number of nodes in the cluster
     *
     * @param value the root element
     * @return the size of the cluster
     */
    public int getTreeSize(int value) {
        return value & 0xFFFF;
    }

    /**
     * Splits an integer into bytes to store the size and a check for
     * if the node is a root
     *
     * @param size the size of the cluster
     * @return the converted integer
     */
    public int getRootValue(int size) {
        return 0x80000000 | size;
    }

    /**
     * Get the overall size of the disjoint set
     *
     * @return the size of the disjoint set
     */
    public int getSize() {
        return this.set.length;
    }

    /**
     * Count the total number of sets
     *
     * @return the number of sets
     */
    public int countSets() {
        int count = 0;
        for(int i = 0; i < this.getSize(); i++)
            if(this.isRoot(i)) count++;
        return count;
    }

    /**
     * Calculates the difference of the cluster size from the mean cluster size
     *
     * @param average the mean cluster size of clusters in the disjont set
     * @param size the size of the current cluster
     * @return the difference from the mean
     */
    public double diffFromAverage(double average, int size) {
        return (size - average) / size;
    }

    /**
     * Count the clusters in the union, but filter
     * the outliers from the set
     *
     * @return number of clusters after outliers have been filtered
     */
    public int filterOutliers() {
        int count = 0;
        double average = this.getAverage();
        for(int i = 0; i < this.getSize(); i++) {
            if(this.isRoot(i)){
                int size = this.getTreeSize(this.set[i]);
                double diffFromAverage = diffFromAverage(average, this.getTreeSize(this.set[i]));
                if(diffFromAverage > 0.75) {
                    count += size / average;
                    this.roots.add(i);
                }
                if(diffFromAverage > -0.75 && diffFromAverage < 0.75) {
                    count++;
                    this.roots.add(i);
                }
            }
        }
        return count;
    }

    /**
     * Calculate the mean cluster size
     *
     */
    public void calculateAverage() {
        int birdSize = 0;

        for(int i = 0; i < this.getSize(); i++) {
            if(this.isRoot(i)) {
                int size = getTreeSize(this.set[i]);
                birdSize = birdSize + size;
            }
        }

        this.average = (double) birdSize / this.countSets();
    }

    /**
     * Get the value of a set by the index
     *
     * @param index of the disjoint set
     * @return value of the element at the provided index
     */
    public int getValue(int index) {
        return this.set[index];
    }

    /**
     * Get a list of all the indices of all the roots
     * in the disjoint set
     *
     * @return list of indices of the roots in the set
     */
    public ArrayList<Integer> getRootIndices() {
        return this.roots;
    }

    /**
     * pretty print the disjoint set
     *
     */
    @Override
    public String toString() {
        String str = "";

        for(int y = 0; y < height; y++) {

            for(int x = 0; x < width; x++) {

                int current = x + y * width;
                int value = isRoot(current) ? -9 : set[current];

                str += " | " + value + " | ";

            }
            str += "\n";
        }

        return str;
    }

}

