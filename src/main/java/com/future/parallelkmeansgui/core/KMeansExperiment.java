package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.model.Result;
import javafx.util.Pair;

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

    private static final int MAX_ITERATION = 100;
    private static final int K = 20;
    private static final double TOLERANCE = 0.1;
    public KMeansExperiment(List<Point> dataset) {
        this.dataset = dataset;
    }

    public void runExperiments() {
        // this method populates all properties in this class
        List<Point> sseVsKSequential = new ArrayList<>();
        List<Point> sseVsKParallel = new ArrayList<>();
        List<Point> runtimeVsKSequential = new ArrayList<>();
        List<Point> runtimeVsKParallel = new ArrayList<>();
        List<Result> kmeansSequentialResults = getKmeansSequentialResults(K);
        List<Result> kmeansParallelResults = getKmeansParallelResults(K);
        for (int i = 1; i <= K; i++) {
            sseVsKSequential.add(new Point(i,kmeansParallelResults.get(i).sse()));
            runtimeVsKSequential.add(new Point(i,kmeansParallelResults.get(i).runtime()));
            sseVsKParallel.add(new Point(i,kmeansSequentialResults.get(i).sse()));
            runtimeVsKParallel.add(new Point(i,kmeansSequentialResults.get(i).runtime()));
        }
        Result bestKmeansParallelResult = ElbowKGetter.getBestResult(getKmeansParallelResults(K));
        Result bestKmeansSequentialResult = ElbowKGetter.getBestResult(getKmeansSequentialResults(K));

        this.bestParallelClusters = bestKmeansParallelResult.clusters();
        this.bestSequentialClusters = bestKmeansSequentialResult.clusters();
        this.sseVsKSequential = sseVsKSequential;
        this.sseVsKParallel = sseVsKParallel;
        this.bestSSEVsKPointParallel = new Point(bestKmeansParallelResult.k(), bestKmeansParallelResult.sse());
        this.bestSSEVsKPointSequential = new Point(bestKmeansSequentialResult.k(), bestKmeansSequentialResult.sse());
        this.runtimeVsKSequential = runtimeVsKSequential;
        this.runtimeVsKParallel = runtimeVsKParallel;
    }

    private List<Result> getKmeansSequentialResults(int k){
        List<Result> results = new ArrayList<>();
        for (int i = 1; i <= K; i++){
        KMeans kmeans = new KMeansSequentialImpl(new KMeansConfig(dataset, i, MAX_ITERATION, TOLERANCE));
        Result result = kmeans.runKMeans();
        results.add(result);
        }
        return results;
    }

    private List<Result> getKmeansParallelResults(int k){
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
