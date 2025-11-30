package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

public class CentroidReComputer {

    public static Cluster reCompute(Cluster cluster) {
        List<Point> points = cluster.points();

        // Edge case: If no points assigned, keep the old centroid to prevent NaN
        if (points == null || points.isEmpty()) {
            return new Cluster(cluster.centroid(), new ArrayList<>());
        }

        double sumX = 0;
        double sumY = 0;

        for (Point p : points) {
            sumX += p.x();
            sumY += p.y();
        }

        double newX = sumX / points.size();
        double newY = sumY / points.size();

        // Return new cluster with updated centroid and fresh empty list for next pass
        return new Cluster(new Point(newX, newY), new ArrayList<>());
    }

    public static boolean hasClustersConverged(List<Cluster> oldClusters, List<Cluster> newClusters, double tolerance) {
        if (oldClusters.size() != newClusters.size()) {
            return false;
        }

        double maxMovement = 0.0;

        for (int i = 0; i < oldClusters.size(); i++) {
            Point oldCentroid = oldClusters.get(i).centroid();
            Point newCentroid = newClusters.get(i).centroid();

            double distance = DistanceCalculator.calculate(oldCentroid, newCentroid);
            if (distance > maxMovement) {
                maxMovement = distance;
            }
        }

        // If the max movement is smaller than tolerance, we have converged
        return maxMovement < tolerance;
    }
}
