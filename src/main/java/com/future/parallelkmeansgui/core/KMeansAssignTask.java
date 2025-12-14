package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class KMeansAssignTask extends RecursiveTask<List<Cluster>> {

    private static final int THRESHOLD = 50_000;

    private final List<Point> points;
    private final int start;
    private final int end;
    private final List<Cluster> clusters;

    public KMeansAssignTask(List<Point> points, int start, int end, List<Cluster> clusters) {
        this.points = points;
        this.start = start;
        this.end = end;
        this.clusters = clusters;
    }

    @Override
    protected List<Cluster> compute() {
        int len = end - start;

        if (len <= THRESHOLD) {
            List<Cluster> result = new ArrayList<>(clusters.size());
            for (Cluster cluster : clusters) {
                result.add(new Cluster(cluster.centroid(), new ArrayList<>()));
            }
            for (int i = start; i < end; i++) {
                Point p = points.get(i);
                PointAssigner.assignToNearestCluster(p, result);
            }
            return result;
        } else {
            int mid = start + len / 2;
            KMeansAssignTask left = new KMeansAssignTask(points, start, mid, clusters);
            KMeansAssignTask right = new KMeansAssignTask(points, mid, end, clusters);

            left.fork();
            right.fork();

            return merge(left.join(), right.join());
        }
    }

    private static List<Cluster> merge(List<Cluster> left, List<Cluster> right) {
        for (int i = 0; i < left.size(); i++) {
            left.get(i).points().addAll(right.get(i).points());
        }
        return left;
    }
}

