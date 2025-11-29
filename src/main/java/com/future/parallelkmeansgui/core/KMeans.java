package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.util.RuntimeTimer;

import java.util.List;

public abstract class KMeans {

    protected final KMeansConfig config;
    protected long runtime;
    protected double sse;

    public KMeans(KMeansConfig config) {
        this.config = config;
    }

    protected abstract List<Cluster> fit();

    public List<Cluster> getClusters() {
        RuntimeTimer timer = new RuntimeTimer();
        timer.start();
        List<Cluster> clusters = fit();
        this.runtime = timer.stop();
        this.sse = SSEComputer.compute(clusters);
        return clusters;
    }

    public long getRuntime() {
        return runtime;
    }

    public double getSSE() {
        return sse;
    }
}
