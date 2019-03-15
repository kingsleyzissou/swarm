package io.swarm.stats;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class BestFit implements Serializable {

    private double rSquared, slope, intercept;

    private int x1, x2;

    public BestFit(double rSquared, double slope, double intercept, int x1, int x2) {
        this.rSquared = rSquared;
        this.slope = slope;
        this.intercept = intercept;
        this.x1 = x1;
        this.x2 = x2;
    }

    /**
     * Get the pearson correlation coefficient for the line
     * of best fit
     *
     * @return pearson correlation coefficient
     */
    public double getRSquared() {
        return rSquared;
    }

    /**
     * Get the slope of the line of best fit
     *
     * @return slope of the line of best fit
     */
    public double getSlope() {
        return slope;
    }

    /**
     * Determine the angle between two slopes
     *
     * @param m1 slope 1
     * @param m2 slope 2
     * @return degree in radians between the two slopes
     */
    public double angleBetweenSlopes(double m1, double m2) {
        double tanTheta = Math.abs((m1 - m2) / 1 + (m1*m2));
        return Math.atan(tanTheta);
    }

    /**
     * Determine the angle between the slope of this object and
     * another BestFit object
     *
     * @param comparisonLine line to be compared with
     * @return angle between lines
     */
    public double angleBetweenSlopes(BestFit comparisonLine) {
        if(!comparisonLine.isGoodFit()) return 0;
        double angle = angleBetweenSlopes(slope, comparisonLine.getSlope());
        System.out.println(Math.toDegrees(angle));
        return (angle > Math.PI / 6 && angle < Math.PI/2) ? angle : -1;
    }

    /**
     * Determine if the line of best fit is a good fit
     *
     * @return true or false
     */
    public boolean isGoodFit() {
        return ((rSquared > 0.9 && rSquared < 1.1) || (rSquared > -1.1 && rSquared < -0.9));
    }

    /**
     * Construct a JavaFX line shape from the BestFit line object
     *
     * @return JavaFX line object
     */
    public Line constructLine() {
        Line line = new Line(x1, slope * x1 + intercept, x2, (x2) * slope + intercept);
        line.setStroke(Color.BLUE);
        return line;
    }

}
