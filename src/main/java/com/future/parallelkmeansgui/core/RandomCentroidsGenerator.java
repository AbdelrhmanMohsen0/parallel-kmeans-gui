package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Point;

import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class RandomCentroidsGenerator {

    public static List<Point> generateKCentroids(List<Point> points, int k) {

        // Handling edge cases
        if (points == null || points.isEmpty() || k <= 0) {
            return List.of();
        }

        // Find bounding box
        double minX = points.stream().mapToDouble(Point::x).min().getAsDouble();
        double maxX = points.stream().mapToDouble(Point::x).max().getAsDouble();
        double minY = points.stream().mapToDouble(Point::y).min().getAsDouble();
        double maxY = points.stream().mapToDouble(Point::y).max().getAsDouble();

        // Generate unique centroids
        Random random = new Random();
        Set<Point> centroids = new HashSet<>();

        while (centroids.size() < k) {
            double randomX = minX + (maxX - minX) * random.nextDouble();
            double randomY = minY + (maxY - minY) * random.nextDouble();
            centroids.add(new Point(randomX, randomY));
        }

        return List.copyOf(centroids);
    }
}
