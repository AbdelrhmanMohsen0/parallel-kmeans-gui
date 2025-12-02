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

    @Override
    protected List<Cluster> fit() {

        List<Point> initialCentroids = RandomCentroidsGenerator.generateKCentroids(points, config.k());

        List<Cluster> clusters = new ArrayList<>();
        for (Point centroid : initialCentroids) {
            clusters.add(new Cluster(centroid, new ArrayList<>()));
        }

        // 2. Main Loop
        for (int i = 0; i < config.maxIterations(); i++) {


            for (Point point : points) {
                PointAssigner.assignToNearestCluster(point, clusters);
            }


            List<Cluster> newClusters = new ArrayList<>();
            for (Cluster cluster : clusters) {

                newClusters.add(CentroidReComputer.reCompute(cluster));
            }


            if (CentroidReComputer.hasClustersConverged(clusters, newClusters, config.tolerance())) {
                return clusters;
            }


            clusters = newClusters;
        }

        for (Point point : points) {
            PointAssigner.assignToNearestCluster(point, clusters);
        }

        return clusters;
    }

    /**
     * BONUS FEATURE: Multiple Random Restarts.
     * Runs the K-Means algorithm multiple times and returns the result with the lowest SSE.
     * @return The best cluster configuration found.
     */
    public List<Cluster> fitWithRestarts(int trials) {
        List<Cluster> bestClusters = null;
        double minSSE = Double.MAX_VALUE;

        for (int i = 0; i < trials; i++) {
            // Run the standard single-pass algorithm
            List<Cluster> currentResult = this.fit();

            // Calculate the error (SSE) for this run
            double currentSSE = SSEComputer.compute(currentResult);

            // If this run is better (lower error) than previous ones, keep it
            if (currentSSE < minSSE) {
                minSSE = currentSSE;
                bestClusters = currentResult;
            }
        }

        return bestClusters;
    }
}