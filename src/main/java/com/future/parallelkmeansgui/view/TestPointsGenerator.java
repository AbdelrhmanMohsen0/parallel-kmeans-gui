package com.future.parallelkmeansgui.view;

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
}
