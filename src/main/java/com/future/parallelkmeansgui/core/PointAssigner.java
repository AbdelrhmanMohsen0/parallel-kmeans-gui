package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.util.DistanceCalculator;

import java.util.List;

public class PointAssigner {

    public static void assignToNearestCluster(Point point, List<Cluster> clusters) {

        if (clusters == null || clusters.isEmpty() || point == null) {
            throw new IllegalArgumentException("Point and clusters must not be null or empty.");
        }

        Cluster nearestCluster = null;
        double minDistance = Double.MAX_VALUE;

        for (Cluster cluster : clusters) {
            double distance = DistanceCalculator.calculate(point, cluster.centroid());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }

        if (nearestCluster != null) {
            nearestCluster.points().add(point); // assumes points() returns a mutable list
        }
    }

    public static int getNearestCentroidIndex(Point point, List<Point> centroids) {
        if (centroids == null || centroids.isEmpty() || point == null) {
            throw new IllegalArgumentException("Point and clusters must not be null or empty.");
        }

        int nearestCentroidIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centroids.size(); i++) {
            double distance = DistanceCalculator.calculate(point, centroids.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                nearestCentroidIndex = i;
            }
        }

        return nearestCentroidIndex;
    }
}
