package io.helpers;

import io.swarm.DisjointSet;
import javafx.scene.shape.Line;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FlockRegression {

    private DisjointSet set;
    private int width, height;

    private ArrayList<ArrayList<double[]>> segments = new ArrayList<>();

    private ArrayList<double[]> all = new ArrayList<>();
    private ArrayList<double[]> x1 = new ArrayList<>(), x2 = new ArrayList<>();
    private ArrayList<double[]> y1 = new ArrayList<>(), y2 = new ArrayList<>();

    public FlockRegression(DisjointSet set, int width, int height) {
        this. set = set;
        this.width = width;
        this.height = height;
        Collections.addAll(segments, x1, x2, y1, y2, all);
        buildSegments();
    }

    public ArrayList<ArrayList<double[]>> buildSegments() {

        for (Integer root : set.getRootIndices()) {
            double x = IndexCoordinate.getXCoordinate(root, set.getWidth());
            double y = IndexCoordinate.getYCoordinate(root, set.getWidth());
            partition(x, y , segments);
        }

        return segments;

    }

    private void partition(double x, double y, ArrayList<ArrayList<double[]>> quadrants) {
        double[] xy = new double[]{x, y};

        int halfX = IndexCoordinate.getXCoordinate((set.getSize() - 1)/2, width);
        int halfY = IndexCoordinate.getYCoordinate((set.getSize() - 1)/2, width);

        if(x < halfX) { quadrants.get(0).add(xy); }
        if(x > halfX) { quadrants.get(1).add(xy); }
        if(y < halfY) { quadrants.get(2).add(xy); }
        if(y > halfY) { quadrants.get(3).add(xy); }
        quadrants.get(4).add(xy);
    }

    

    public ArrayList<RegressionAnalysis> analyseRegression() {
        ArrayList<RegressionAnalysis> results = new ArrayList<>();
        for(ArrayList<double[]> segment : segments) {
            double[][] stuff = segment.toArray(new double[segment.size()][]);
            results.add(linearRegression(stuff));
        }
        return results;
    }

    public ArrayList<Line> compareSegments() {

        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<RegressionAnalysis> results = this.analyseRegression();

        boolean x = results.get(0).perpendicularityTest(results.get(1).getSlope());
        boolean y = results.get(2).perpendicularityTest(results.get(3).getSlope());

        if(x) Collections.addAll(lines, results.get(0).constructLine(), results.get(1).constructLine());
        if(y) Collections.addAll(lines, results.get(2).constructLine(), results.get(3).constructLine());

        return lines;

    }

    public RegressionAnalysis overAllRegression() {
        ArrayList<double[]> allCoordinates = segments.get(4);
        double[][] all = allCoordinates.toArray(new double[allCoordinates.size()][]);
        return linearRegression(all);
    }

    public RegressionAnalysis linearRegression(double[][] coordinates) {
        SimpleRegression regression = new SimpleRegression(true);
        regression.addData(coordinates);
        int x1 = 0, x2 = set.getSize() - 1;
        return new RegressionAnalysis(regression.getR(), regression.getSlope(), regression.getIntercept(), x1, x2);

//        System.out.println("slope = " + regression.getSlope());
//        System.out.println("intercept = " + regression.getIntercept());

        // trying to run model for unknown data
//        System.out.println("prediction for 1.5 = " + regression.predict(1.5));
//        System.out.println("r = " + regression.getR());


    }

}
