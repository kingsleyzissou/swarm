package io.swarm.collections;

import io.swarm.stats.ClusterAnalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class DisjointSet implements Serializable {

    private int[] set;
    private int height, width;
    private HashSet<Integer> roots;
    private ClusterAnalysis statistics;

    public DisjointSet(int width, int height) {
        this.width = width;
        this.height = height;
        this.set = new int[width * height];
        roots = new HashSet<>();
    }

    public DisjointSet(double width, double height) {
        this.width = (int) width;
        this.height = (int) height;
        this.set = new int[(int) width * (int) height];
        roots = new HashSet<>();
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
        int value = (index >= 0) ? this.set[index] : index;
        if(value == -1) return false;
        return Math.abs((value >> 31)) == 1;
    }

    /**
     * Find the root of a disjoint set node with path compression
     *
     * @param index the node for which the parent is being searched
     * @return the parent of the index node
     */
    public int find(int index) {
        return isRoot(index) ? index : (set[index] = find(set[index]));
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
            int size = clusterSize(this.set[rootA]) + clusterSize(this.set[rootB]);
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
    public int clusterSize(int value) {
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
    public void gatherRoots() {
        for(int i = 0; i < this.getSize(); i++)
            if(this.isRoot(i)) roots.add(i);
    }

    /**
     * Count the number of roots in the set
     *
     * @return size of the roots in the set
     */
    public int countSets() {
        gatherRoots();
        return noiseReducedCount();
    }

    /**
     * Count the clusters in the union, but filter
     * the outliers from the set
     *
     */
    public int noiseReducedCount() {
        // Gather statistical information required for the method
        if(statistics == null) statistics = new ClusterAnalysis(getSerializedClusterSizes());
        // Get all the roots if this has not been done yet
        if(roots.isEmpty()) gatherRoots();
        // Create a list for items to be removed
        ArrayList<Integer> toRemove = new ArrayList<>();
        int count = 0;
        for(Integer index : roots) {
            int size = clusterSize(getValue(index));
            // Get the weight of each cluster
            int weight = statistics.weight(size);
            // Remove outlier
            if(weight == 0) toRemove.add(index);
            // Or increase the count based on the weight of the cluster
            if(weight != 0) count = count + weight;
        }
        // Remove all outliers from the HashSet
        roots.removeAll(toRemove);
        return count;
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
    public HashSet<Integer> getRootIndices() {
        return this.roots;
    }



    public double[] getSerializedClusterSizes() {
            ArrayList<Double> clusters = new ArrayList<>();
            Double[] serialisedClusters = new Double[this.roots.size()];
            for(int index: roots) {
                double size = this.clusterSize(getValue(index));
                clusters.add(size);
            }
            return Arrays.stream(clusters.toArray(serialisedClusters)).mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Begin merging the clusters in the set by iterating through the set
     * and merging elements that are adjacent to each other
     */
    public void generateClusters() {
        if(roots != null || !roots.isEmpty()) roots.clear();
        for(int index = 0; index < getSize(); index++) {
            int right = index + 1;
            int down = index +  width;
            if(getValue(index) != -1) {
                if(right < getSize() - 1 && getValue(right) != -1)
                    quickUnion(index, right);
                if(down < getSize() - 1 && getValue(down) != -1)
                    quickUnion(index, down);
            }
        }
    }

    /**
     * Pretty print the disjoint set
     *
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int current = x + y * width;
                int value = isRoot(current) ? -9 : set[current];
                str.append(" | ").append(value).append(" | ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Returns the statistical analysis of the size of the clusters
     *
     * @return basic statistic wrapper for the size of the clusters
     */
    public ClusterAnalysis getStatistics() {
        return statistics;
    }

    /**
     * Get the width of the corresponding image to the disjoint set
     *
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Replace the set and start over
     *
     * @param array new array to be added to the set
     */
    public void replaceSet(int[] array) {
        if(array.length == set.length) this.set = array;
    }
}

