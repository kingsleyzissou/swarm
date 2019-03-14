package io.swarm.stats;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class BestFit implements Serializable {

    private double rSquared;
    private double slope;
    private double intercept;

    private int x1;
    private int x2;

    public BestFit(double rSquared, double slope, double intercept, int x1, int x2) {
        this.rSquared = rSquared;
        this.slope = slope;
        this.intercept = intercept;
        this.x1 = x1;
        this.x2 = x2;
    }

    public double getRSquared() {
        return rSquared;
    }

    public double getSlope() {
        return slope;
    }

    public double angleBetweenSlopes(double m1, double m2) {
        double tanTheta = Math.abs((m1 - m2) / 1 + (m1*m2));
        return Math.atan(tanTheta);
    }

    public double angleBetweenSlopes(BestFit comparisonLine) {
        if(!comparisonLine.isGoodFit()) return 0;
        double angle = angleBetweenSlopes(slope, comparisonLine.getSlope());
        return (angle > Math.PI / 4 && angle < Math.PI/2) ? angle : -1;
    }

    public boolean isGoodFit() {
        return ((rSquared > 0.9 && rSquared < 1.1) || (rSquared > -1.1 && rSquared < -0.9));
    }

    public Line constructLine() {
        Line line = new Line(x1, slope * x1 + intercept, x2, (x2) * slope + intercept);
        line.setStroke(Color.BLUE);
        return line;
    }

}
