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

    /**
     * BONUS FEATURE: Multiple Random Restarts.
     */
    public Result runKMeansWithRestarts(int trials) {
        Result bestResult = null;
        double minSSE = Double.MAX_VALUE;

        for (int i = 0; i < trials; i++) {

            Result currentResult = runKMeans();

            if (currentResult.sse() < minSSE) {
                minSSE = currentResult.sse();
                bestResult = currentResult;
            }
        }
        return bestResult;
    }
}