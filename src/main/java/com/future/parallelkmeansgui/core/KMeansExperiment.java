package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.model.Result;

import java.util.List;

public class KMeansExperiment {

    private final List<Point> dataset;
    private List<Cluster> bestSequentialClusters;
    private List<Cluster> bestParallelClusters;
    private List<Point> sseVsKSequential;
    private List<Point> sseVsKParallel;
    private Point bestSSEVsKPointSequential;
    private Point bestSSEVsKPointParallel;
    private List<Point> runtimeVsKSequential;
    private List<Point> runtimeVsKParallel;

    public KMeansExperiment(List<Point> dataset) {
        this.dataset = dataset;
    }

    public void runExperiments() {
        // Just and example, this method should populate all properties in this class
        KMeans kmeans = new KMeansSequentialImpl(new KMeansConfig(dataset, 3, 1000, 0.1));
        Result result = kmeans.runKMeans();
        bestSequentialClusters = result.clusters();
    }

    public List<Cluster> getBestSequentialClusters() {
        return bestSequentialClusters;
    }

    public List<Cluster> getBestParallelClusters() {
        return bestParallelClusters;
    }

    public List<Point> getSseVsKSequential() {
        return sseVsKSequential;
    }

    public List<Point> getSseVsKParallel() {
        return sseVsKParallel;
    }

    public List<Point> getRuntimeVsKSequential() {
        return runtimeVsKSequential;
    }

    public List<Point> getRuntimeVsKParallel() {
        return runtimeVsKParallel;
    }

    public Point getBestSSEVsKPointSequential() {
        return bestSSEVsKPointSequential;
    }

    public Point getBestSSEVsKPointParallel() {
        return bestSSEVsKPointParallel;
    }

}
