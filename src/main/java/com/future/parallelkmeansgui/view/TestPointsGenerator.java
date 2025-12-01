package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestPointsGenerator {


    public static List<Point> generateElbowMethodTest() {
        List<Point> points = new ArrayList<>();
        Random random = new Random();

        double baseSSE = 200.0;
        for (int k = 1; k <= 20; k++) {
            double sse = baseSSE / k + random.nextDouble() * 10;
            points.add(new Point(k, sse));
        }

        return points;
    }

    public static List<Point> generateSequentialRuntimeKTest() {
        int maxK = 20;
        List<Point> points = new ArrayList<>();
        Random random = new Random();

        for (int k = 1; k <= maxK; k++) {
            // Simulate runtime: grows roughly linearly with K
            double runtime = k * 50 + random.nextDouble() * 10;
            points.add(new Point(k, runtime));
        }

        return points;
    }

    public static List<Point> generateParallelRuntimeKTest() {
        int maxK = 20;
        List<Point> points = new ArrayList<>();
        Random random = new Random();

        for (int k = 1; k <= maxK; k++) {
            // Simulate runtime: grows sub-linearly with K
            double runtime = k * 20 + random.nextDouble() * 5;
            points.add(new Point(k, runtime));
        }

        return points;
    }

    public static List<Cluster> generateClusters(int numClusters, int pointsPerCluster) {
        List<Cluster> clusters = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numClusters; i++) {
            // Random centroid position
            double cx = random.nextDouble(); // centroid x
            double cy = random.nextDouble(); // centroid y
            Point centroid = new Point(cx, cy);

            // Generate points around centroid
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < pointsPerCluster; j++) {
                double x = cx + random.nextGaussian() * 0.1; // spread around centroid
                double y = cy + random.nextGaussian() * 0.1;
                points.add(new Point(x, y));
            }

            clusters.add(new Cluster(centroid, points));
        }

        return clusters;
    }
}
