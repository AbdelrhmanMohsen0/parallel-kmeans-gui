package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.model.Result;

import java.util.ArrayList;
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

    public static final int MAX_ITERATION = 2000;
    public static final int K = 20;
    public static final double TOLERANCE = 0.001;

    public KMeansExperiment(List<Point> dataset) {
        this.dataset = dataset;
    }

    public void runExperiments() {
        List<Point> sseVsKSequential = new ArrayList<>();
        List<Point> sseVsKParallel = new ArrayList<>();
        List<Point> runtimeVsKSequential = new ArrayList<>();
        List<Point> runtimeVsKParallel = new ArrayList<>();
        List<Result> kmeansSequentialResults = getKmeansSequentialResults();
        List<Result> kmeansParallelResults = getKmeansParallelResults();

        for (int i = 1, j = 0; i <= K; i++, j++) {
            sseVsKSequential.add(new Point(i,kmeansParallelResults.get(j).sse()));
            runtimeVsKSequential.add(new Point(i,kmeansParallelResults.get(j).runtime()));
            sseVsKParallel.add(new Point(i,kmeansSequentialResults.get(j).sse()));
            runtimeVsKParallel.add(new Point(i,kmeansSequentialResults.get(j).runtime()));
        }

        Result bestKmeansParallelResult = ElbowKGetter.getBestResult(getKmeansParallelResults());
        Result bestKmeansSequentialResult = ElbowKGetter.getBestResult(getKmeansSequentialResults());

        this.bestParallelClusters = bestKmeansParallelResult.clusters();
        this.bestSequentialClusters = bestKmeansSequentialResult.clusters();
        this.sseVsKSequential = sseVsKSequential;
        this.sseVsKParallel = sseVsKParallel;
        this.bestSSEVsKPointParallel = new Point(bestKmeansParallelResult.clusters().size(), bestKmeansParallelResult.sse());
        this.bestSSEVsKPointSequential = new Point(bestKmeansSequentialResult.clusters().size(), bestKmeansSequentialResult.sse());
        this.runtimeVsKSequential = runtimeVsKSequential;
        this.runtimeVsKParallel = runtimeVsKParallel;
    }

    private List<Result> getKmeansSequentialResults(){
        List<Result> results = new ArrayList<>();
        for (int i = 1; i <= K; i++){
            KMeans kmeans = new KMeansSequentialImpl(new KMeansConfig(dataset, i, MAX_ITERATION, TOLERANCE));
            Result result = kmeans.runKMeans();
            results.add(result);
        }
        return results;
    }

    private List<Result> getKmeansParallelResults(){
        List<Result> results = new ArrayList<>();
        for (int i = 1; i <= K; i++){
            KMeans kmeans = new KMeansParallelImpl(new KMeansConfig(dataset, i, MAX_ITERATION, TOLERANCE));
            Result result = kmeans.runKMeans();
            results.add(result);
        }
        return results;
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
