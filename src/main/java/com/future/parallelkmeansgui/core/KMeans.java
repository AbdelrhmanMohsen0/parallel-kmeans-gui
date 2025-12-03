package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Result;
import com.future.parallelkmeansgui.util.RuntimeTimer;

import java.util.List;

public abstract class KMeans {

    protected final KMeansConfig config;

    public KMeans(KMeansConfig config) {
        this.config = config;
    }

    protected abstract List<Cluster> fit();

    public Result runKMeans() {
        RuntimeTimer timer = new RuntimeTimer();
        timer.start();
        List<Cluster> clusters = fit();
        long runtime = timer.stop();
        double sse = SSEComputer.compute(clusters);
        return new Result(clusters, runtime, sse);
    }

}
