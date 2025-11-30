package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;

public class KMeansSequentialImpl extends KMeans {

    private final List<Point> points;

    public KMeansSequentialImpl(KMeansConfig config, List<Point> points) {
        super(config);
        this.points = points;
    }

    @Override
    protected List<Cluster> fit() {
        // 1. Initialize Centroids (Task 2 - Team Member's Code)
        List<Point> initialCentroids = RandomCentroidsGenerator.generateKCentroids(points, config.k());

        // Convert Points to Clusters (Initialize with empty mutable lists)
        List<Cluster> clusters = new ArrayList<>();
        for (Point centroid : initialCentroids) {
            clusters.add(new Cluster(centroid, new ArrayList<>()));
        }

        // 2. Main Loop
        for (int i = 0; i < config.maxIterations(); i++) {

            // Note: If reusing clusters, we would clear points here.
            // But since reCompute returns NEW clusters with empty lists,
            // the lists are already empty at the start of this loop (except for 1st iter created above).

            // 3. Assign points to nearest centroid (Task 3 - Team Member's Code)
            for (Point point : points) {
                PointAssigner.assignToNearestCluster(point, clusters);
            }

            // 4. Recompute Centroids (Task 4)
            List<Cluster> newClusters = new ArrayList<>();
            for (Cluster cluster : clusters) {
                // CentroidReComputer returns a new Cluster with an empty point list
                newClusters.add(CentroidReComputer.reCompute(cluster));
            }

            // 5. Check Convergence (Task 4)
            // If converged, we return 'clusters' (the one with points currently assigned)
            if (CentroidReComputer.hasClustersConverged(clusters, newClusters, config.tolerance())) {
                return clusters;
            }

            // Update for next iteration (newClusters has updated centroids but empty point lists)
            clusters = newClusters;
        }

        // If max iterations reached, we must do one final assignment
        // because 'clusters' currently has empty point lists (from the last reCompute).
        for (Point point : points) {
            PointAssigner.assignToNearestCluster(point, clusters);
        }

        return clusters;
    }
}