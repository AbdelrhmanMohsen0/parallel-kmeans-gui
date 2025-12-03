package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;

public class KMeansSequentialImpl extends KMeans {

    private final List<Point> points;

    public KMeansSequentialImpl(KMeansConfig config) {
        super(config);
        this.points = config.points();
    }

    /**
     * Standard implementation: Runs the algorithm ONCE.
     */
    @Override
    protected List<Cluster> fit() {
        // 1. Initialize Centroids
        List<Point> initialCentroids = RandomCentroidsGenerator.generateKCentroids(points, config.k());

        List<Cluster> clusters = new ArrayList<>();
        for (Point centroid : initialCentroids) {
            clusters.add(new Cluster(centroid, new ArrayList<>()));
        }

        // 2. Main Loop
        for (int i = 0; i < config.maxIterations(); i++) {

            // Assign
            for (Point point : points) {
                PointAssigner.assignToNearestCluster(point, clusters);
            }

            // Recompute
            List<Cluster> newClusters = new ArrayList<>();
            for (Cluster cluster : clusters) {
                newClusters.add(CentroidReComputer.reCompute(cluster));
            }

            // Converge
            if (CentroidReComputer.hasClustersConverged(clusters, newClusters, config.tolerance())) {
                return clusters;
            }

            clusters = newClusters;
        }

        // Final assignment
        for (Point point : points) {
            PointAssigner.assignToNearestCluster(point, clusters);
        }

        return clusters;
    }

    /**
     * BONUS FEATURE: Multiple Random Restarts.
     * Keeps K the same, but tries different random seeds to find the lowest SSE for THAT K.
     */
    public List<Cluster> fitWithRestarts(int trials) {
        List<Cluster> bestClusters = null;
        double minSSE = Double.MAX_VALUE;

        for (int i = 0; i < trials; i++) {
            List<Cluster> currentResult = this.fit();
            double currentSSE = SSEComputer.compute(currentResult);
            if (currentSSE < minSSE) {
                minSSE = currentSSE;
                bestClusters = currentResult;
            }
        }
        return bestClusters;
    }
}