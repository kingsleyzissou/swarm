package io.swarm.stats;

import io.swarm.collections.CoordinateSet;
import io.swarm.collections.DisjointSet;
import javafx.scene.shape.Line;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Collections;

public class FlockRegression {

    private DisjointSet set;
    private int width;

    private ArrayList<CoordinateSet> segments = new ArrayList<>();

    public FlockRegression(DisjointSet set, int width) {
        this. set = set;
        this.width = width;
        CoordinateSet all = new CoordinateSet();
        CoordinateSet x1 = new CoordinateSet(), x2 = new CoordinateSet();
        CoordinateSet y1 = new CoordinateSet(), y2 = new CoordinateSet();
        Collections.addAll(segments, x1, x2, y1, y2, all);
        buildSegments();
    }

    /**
     * Loop through the roots and separate them into four segments (2 horizontal, 2 vertical and all roots)
     * for use in linear regression analysis
     *
     */
    private void buildSegments() {
        for (Integer root : set.getRootIndices()) {
            double x = CoordinateSet.getXCoordinate(root, set.getWidth());
            double y = CoordinateSet.getYCoordinate(root, set.getWidth());
            partition(x, y , segments);
        }
    }

    /**
     * Add coordinates into correct partitions
     *
     * @param x coordinate
     * @param y y coordinate
     * @param segments the segments of coordinates
     */
    private void partition(double x, double y, ArrayList<CoordinateSet> segments) {
        double[] xy = new double[]{x, y};
        // Get the halfway point of x axis for vertical segments
        int halfX = width/2;
        // Get the halfway point of the y axis for horizontal segments
        int halfY = CoordinateSet.getYCoordinate((set.getSize() - 1)/2, width);
        if(x < halfX) { segments.get(0).add(xy); }
        if(x > halfX) { segments.get(1).add(xy); }
        if(y < halfY) { segments.get(2).add(xy); }
        if(y > halfY) { segments.get(3).add(xy); }
        segments.get(4).add(xy);
    }

    /**
     * Analyse the linear regression of the coordinates for
     * each of the segments
     *
     * @return the results from the regression analysis
     */
    private ArrayList<BestFit> analyseRegression() {
        ArrayList<BestFit> results = new ArrayList<>();
        for(CoordinateSet segment : segments) {
            double[][] stuff = segment.to2DArray();
            results.add(linearRegression(stuff));
        }
        return results;
    }

    /**
     * Compare the linear regression of the horizontal segments
     * and compare the results of the vertical segments
     *
     * @return 2 lines in the case of v-formation
     */
    public ArrayList<Line> compareSegments() {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<BestFit> results = this.analyseRegression();
        double x = results.get(0).angleBetweenSlopes(results.get(1));
        double y = results.get(2).angleBetweenSlopes(results.get(3));
        if(x > 0 && x > y)
            Collections.addAll(lines, results.get(0).constructLine(), results.get(1).constructLine());
        if(y > 0 && y > x)
            Collections.addAll(lines, results.get(2).constructLine(), results.get(3).constructLine());
        return lines;
    }

    /**
     * Analyse the regression for the full set of coordinates
     *
     * @return the results of the regression analysis
     */
    public BestFit overAllRegression() {
        CoordinateSet allCoordinates = segments.get(4);
        double[][] all = allCoordinates.to2DArray();
        return linearRegression(all);
    }

    /**
     * Test for linear regression of coordinates in a segment
     *
     * @param coordinates the coordinates of a segment
     * @return BestFit object with the results of the linear regression
     */
    private BestFit linearRegression(double[][] coordinates) {
        SimpleRegression regression = new SimpleRegression(true);
        regression.addData(coordinates);
        int x1 = 0, x2 = set.getSize() - 1;
        return new BestFit(regression.getR(), regression.getSlope(), regression.getIntercept(), x1, x2);
    }

}
