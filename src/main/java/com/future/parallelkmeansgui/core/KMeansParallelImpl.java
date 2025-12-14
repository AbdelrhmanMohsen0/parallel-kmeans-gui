package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class KMeansParallelImpl extends KMeans {

    public KMeansParallelImpl(KMeansConfig config) {
        super(config);
    }

    @Override
    protected List<Cluster> fit() {
        List<Point> points = config.points();
        int k = config.k();
        long maxIterations = config.maxIterations();
        double tolerance = config.tolerance();

        if (points == null || points.isEmpty() || k <= 0) {
            return List.of();
        }

        List<Point> centroids = RandomCentroidsGenerator.generateKCentroids(points, k);

        List<Cluster> clusters = new ArrayList<>(k);
        for (Point c : centroids) {
            clusters.add(new Cluster(c, new ArrayList<>()));
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int iter = 0; iter < maxIterations; iter++) {
            KMeansAssignTask rootTask = new KMeansAssignTask(points, 0, points.size(), clusters);
            clusters = pool.invoke(rootTask);

            List<Cluster> newClusters = new ArrayList<>();
            for (Cluster cluster : clusters) {
                Cluster newCluster = CentroidReComputer.reCompute(cluster);
                newClusters.add(newCluster);
            }

            if (CentroidReComputer.hasClustersConverged(clusters, newClusters, tolerance)) {
                return clusters;
            }

            clusters = newClusters;
        }

        KMeansAssignTask rootTask = new KMeansAssignTask(points, 0, points.size(), clusters);
        clusters = pool.invoke(rootTask);

        return clusters;
    }
}
