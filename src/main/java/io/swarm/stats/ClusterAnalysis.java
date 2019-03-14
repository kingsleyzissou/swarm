package io.swarm.stats;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.Serializable;

public class ClusterAnalysis implements Serializable {

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
//        double sd = standardDeviation();
//        return normalise(observation, mean, sd);
        return (observation - mean) / observation;
    }

    public int weight(int observation) {
        int weight = 1;
        double z = normalise(observation);
        if(z < -0.25 || z > 0.75) weight = 0;
        if(z < 3 && z > 0.75) weight = (int) (observation/mean());
//        if(z < -1 || z > 4.5) weight = 0;
//        if(z < 4.5 && z > 2.5) weight = (int) (observation/mean());
        return weight;
    }

}
