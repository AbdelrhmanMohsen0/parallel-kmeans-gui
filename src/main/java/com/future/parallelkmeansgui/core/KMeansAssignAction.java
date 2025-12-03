package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class KMeansAssignAction extends RecursiveAction {

    private static final int THRESHOLD = 1000;

    private final List<Point> points;
    private final int start;
    private final int end;
    private final List<Cluster> clusters;

    public KMeansAssignAction(List<Point> points, int start, int end, List<Cluster> clusters) {
        this.points = points;
        this.start = start;
        this.end = end;
        this.clusters = clusters;
    }

    @Override
    protected void compute() {
        int len = end - start;
        if (len <= 0) return;

        if (len <= THRESHOLD) {
            for (int i = start; i < end; i++) {
                Point p = points.get(i);
                PointAssigner.assignToNearestCluster(p, clusters);
            }
        } else {
            int mid = start + len / 2;
            KMeansAssignAction left = new KMeansAssignAction(points, start, mid, clusters);
            KMeansAssignAction right = new KMeansAssignAction(points, mid, end, clusters);
            invokeAll(left, right);
        }
    }
}
