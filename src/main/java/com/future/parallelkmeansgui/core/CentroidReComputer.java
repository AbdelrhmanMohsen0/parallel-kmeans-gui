package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;

import java.util.List;

public class CentroidReComputer {

    public static Cluster reCompute(Cluster cluster) {
        return cluster;
    }


    public boolean hasClustersConverged(List<Cluster> oldClusters, List<Cluster> newClusters, double tolerance) {
        return true;
    }
}
