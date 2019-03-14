package io.swarm.stats;

import io.swarm.utilities.IndexCoordinate;
import io.swarm.collections.DisjointSet;
import javafx.scene.shape.Line;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Collections;

public class FlockRegression {

    private DisjointSet set;
    private int width;

    private ArrayList<ArrayList<double[]>> segments = new ArrayList<>();

    private ArrayList<double[]> all = new ArrayList<>();
    private ArrayList<double[]> x1 = new ArrayList<>(), x2 = new ArrayList<>();
    private ArrayList<double[]> y1 = new ArrayList<>(), y2 = new ArrayList<>();

    public FlockRegression(DisjointSet set, int width) {
        this. set = set;
        this.width = width;
        Collections.addAll(segments, x1, x2, y1, y2, all);
        buildSegments();
    }

    public void buildSegments() {
        for (Integer root : set.getRootIndices()) {
            double x = IndexCoordinate.getXCoordinate(root, set.getWidth());
            double y = IndexCoordinate.getYCoordinate(root, set.getWidth());
            partition(x, y , segments);
        }
    }

    private void partition(double x, double y, ArrayList<ArrayList<double[]>> quadrants) {
        double[] xy = new double[]{x, y};

        int halfX = width/2;
        int halfY = IndexCoordinate.getYCoordinate((set.getSize() - 1)/2, width);

        if(x < halfX) { quadrants.get(0).add(xy); }
        if(x > halfX) { quadrants.get(1).add(xy); }
        if(y < halfY) { quadrants.get(2).add(xy); }
        if(y > halfY) { quadrants.get(3).add(xy); }
        quadrants.get(4).add(xy);
    }

    public ArrayList<BestFit> analyseRegression() {
        ArrayList<BestFit> results = new ArrayList<>();
        for(ArrayList<double[]> segment : segments) {
            double[][] stuff = segment.toArray(new double[segment.size()][]);
            results.add(linearRegression(stuff));
        }
        return results;
    }

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

    public BestFit overAllRegression() {
        ArrayList<double[]> allCoordinates = segments.get(4);
        double[][] all = allCoordinates.toArray(new double[allCoordinates.size()][]);
        return linearRegression(all);
    }

    public BestFit linearRegression(double[][] coordinates) {
        SimpleRegression regression = new SimpleRegression(true);
        regression.addData(coordinates);
        int x1 = 0, x2 = set.getSize() - 1;
        return new BestFit(regression.getR(), regression.getSlope(), regression.getIntercept(), x1, x2);
    }

}
