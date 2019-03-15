package io.swarm.stats;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.Serializable;

public class ClusterAnalysis implements Serializable {

    private double[] observations;

    public ClusterAnalysis(double[] observations) {
        this.observations = observations;
    }

    /**
     * Get the mean cluster size
     *
     * @return the mean size of a cluster
     */
    public double mean() {
        Mean mean = new Mean();
        return mean.evaluate(observations);
    }

    /**
     * Get the size of the cluster in comparison to the mean
     *
     * @param observation size of individual cluster
     * @return normalised cluster size
     */
    public double normalise(int observation) {
        double mean = mean();
        return (observation - mean) / observation;
    }

    /**
     * Calculate the weight of a cluster in comparison to
     * the mean and filter outliers
     *
     * @param observation size of individual cluster
     * @return weight of the cluster in comparison to the mean
     */
    public int weight(int observation) {
        int weight = 1;
        double z = normalise(observation);
        if(z < -0.25 || z > 0.75) weight = 0;
        if(z < 3 && z > 0.75) weight = (int) (observation/mean());
        return weight;
    }

}
