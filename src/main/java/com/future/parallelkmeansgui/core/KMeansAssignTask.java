package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class KMeansAssignTask extends RecursiveTask<List<List<Point>>> {

    private static final int THRESHOLD = 50_000;

    private final List<Point> points;
    private final int start;
    private final int end;
    private final List<Point> centroids;

    public KMeansAssignTask(List<Point> points, int start, int end, List<Point> centroids) {
        this.points = points;
        this.start = start;
        this.end = end;
        this.centroids = centroids;
    }

    @Override
    protected List<List<Point>> compute() {
        List<List<Point>> result = new ArrayList<>(centroids.size());
        for (int i = 0; i < centroids.size(); i++) {
            result.add(new ArrayList<>());
        }
        int len = end - start;
        if (len <= 0) return result;

        if (len <= THRESHOLD) {
            for (int i = start; i < end; i++) {
                Point p = points.get(i);
                int centroidIndex = PointAssigner.getNearestCentroidIndex(p, centroids);
                result.get(centroidIndex).add(p);
            }
            return result;
        } else {
            int mid = start + len / 2;
            KMeansAssignTask left = new KMeansAssignTask(points, start, mid, centroids);
            KMeansAssignTask right = new KMeansAssignTask(points, mid, end, centroids);
            left.fork();
            List<List<Point>> rightResult = right.compute();

            List<List<Point>> leftResult = left.join();
            return merge(leftResult, rightResult);
        }
    }

    private static List<List<Point>> merge(List<List<Point>> left, List<List<Point>> right) {
        for (int i = 0; i < left.size(); i++) {
            left.get(i).addAll(right.get(i));
        }
        return left;
    }
}

