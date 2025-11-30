package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.util.DistanceCalculator;

import java.util.List;

public class SSEComputer {

    public static double compute(List<Cluster> clusters) {
        double totalSSE = 0.0;

        for (Cluster cluster : clusters) {
            Point centroid = cluster.centroid();
            List<Point> points = cluster.points();

            if (points == null || points.isEmpty()) {
                continue;
            }

            for (Point point : points) {
                double distance = DistanceCalculator.calculate(point, centroid);
                totalSSE += (distance * distance);
            }
        }

        return totalSSE;
    }
}

