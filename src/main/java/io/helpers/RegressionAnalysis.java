package io.helpers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class RegressionAnalysis {

    private double rsquared;
    private double slope;
    private double intercept;

    private int x1;
    private int x2;

    public RegressionAnalysis(double rsquared, double slope, double intercept, int x1, int x2) {
        this.rsquared = rsquared;
        this.slope = slope;
        this.intercept = intercept;
        this.x1 = x1;
        this.x2 = x2;
    }


    public double getRSquared() {
        return rsquared;
    }

    public void setRSquared(double rsquared) {
        this.rsquared = rsquared;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public void setIntercept(double intercept) {
        this.intercept = intercept;
    }

    public boolean perpendicularityTest(double comparisonSlope) {
        double angle = angleBetweenLines(this.slope, comparisonSlope);
        return (angle > Math.PI / 3 && angle < Math.PI/2);
    }

    public double angleBetweenLines(double m1, double m2) {
        double tanTheta = Math.abs((m1 - m2) / 1 + (m1*m2));
        return Math.atan(tanTheta);
    }

    public Line constructLine() {
        Line line = new Line(x1, slope * x1 + intercept, x2, (x2) * slope + intercept);
        line.setStroke(Color.BLUE);
        return line;
    }

}
