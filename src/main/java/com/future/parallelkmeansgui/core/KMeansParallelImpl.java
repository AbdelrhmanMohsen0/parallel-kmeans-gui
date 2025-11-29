package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;

import java.util.List;

public class KMeansParallelImpl extends KMeans {

    public KMeansParallelImpl(KMeansConfig config) {
        super(config);
    }

    @Override
    protected List<Cluster> fit() {
        int k = config.k();
        List<Point> points = config.points();
        return List.of();
    }

}
