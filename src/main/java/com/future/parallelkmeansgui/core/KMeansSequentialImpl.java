package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;

public class KMeansSequentialImpl extends KMeans {


    public KMeansSequentialImpl(KMeansConfig config) {
        super(config);
    }

    @Override
    protected List<Cluster> fit() {

        List<Point> points = config.points();

        List<Point> initialCentroids = RandomCentroidsGenerator.generateKCentroids(points, config.k());

        List<Cluster> clusters = new ArrayList<>();
        for (Point centroid : initialCentroids) {
            clusters.add(new Cluster(centroid, new ArrayList<>()));
        }

        for (int i = 0; i < config.maxIterations(); i++) {

            for (Point point : points) {
                PointAssigner.assignToNearestCluster(point, clusters);
            }

            List<Cluster> newClusters = new ArrayList<>();
            for (Cluster cluster : clusters) {
                newClusters.add(CentroidReComputer.reCompute(cluster));
            }

            if (CentroidReComputer.hasClustersConverged(clusters, newClusters, config.tolerance())) {
                return clusters;
            }

            clusters = newClusters;
        }

        for (Point point : points) {
            PointAssigner.assignToNearestCluster(point, clusters);
        }

        return clusters;
    }
}