package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.Collections;
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
            List<Point> pts = Collections.synchronizedList(new ArrayList<>());
            clusters.add(new Cluster(c, pts));
        }

        ForkJoinPool pool = ForkJoinPool.commonPool();
        List<Cluster> oldClusters = null;

        for (int iter = 0; iter < maxIterations; iter++) {
            for (Cluster cluster : clusters) {
                cluster.points().clear();
            }

            KMeansAssignAction rootTask = new KMeansAssignAction(points, 0, points.size(), clusters);
            pool.invoke(rootTask);

            List<Cluster> newClusters = new ArrayList<>(clusters.size());
            for (Cluster cluster : clusters) {
                Cluster updated = CentroidReComputer.reCompute(cluster);
                List<Point> synchronizedList = Collections.synchronizedList(new ArrayList<>());
                newClusters.add(new Cluster(updated.centroid(), synchronizedList));
            }

            if (oldClusters != null && CentroidReComputer.hasClustersConverged(oldClusters, newClusters, tolerance)) {
                List<Cluster> finalClusters = new ArrayList<>(clusters.size());
                for (int i = 0; i < clusters.size(); i++) {
                    Cluster current = clusters.get(i);
                    Point newCentroid = newClusters.get(i).centroid();
                    finalClusters.add(new Cluster(newCentroid, current.points()));
                }
                return finalClusters;
            }

            oldClusters = clusters;
            clusters = newClusters;
        }

        for (Cluster cluster : clusters) {
            cluster.points().clear();
        }
        KMeansAssignAction finalAssign = new KMeansAssignAction(points, 0, points.size(), clusters);
        pool.invoke(finalAssign);

        return clusters;
    }
}
