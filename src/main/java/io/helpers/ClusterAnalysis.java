package io.helpers;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class ClusterAnalysis {

    private double[] observations;

    public ClusterAnalysis(double[] observations) {
        this.observations = observations;
    }

    public double mean() {
        Mean mean = new Mean();
        return mean.evaluate(observations);
    }

    public double standardDeviation() {
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(observations);
    }

    public double normalise(int observation, double mean, double sd) {
        return (observation - mean) / sd;
    }

    public double normalise(int observation) {
        double mean = mean();
        double sd = standardDeviation();
        return normalise(observation, mean, sd);
    }

    public int weight(int observation) {
        int weight = 1;
        double z = normalise(observation);
        if(z < -2 || z > 4) weight = 0;
        if(z < 4 && z > 2) weight = (int) (observation/mean());
        return weight;
    }

}
