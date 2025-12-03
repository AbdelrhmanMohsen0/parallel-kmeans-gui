package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Point;

import java.util.List;

public class KMeansMultiExperiment {

    private List<Point> runtimeVsDatasetSizeSequential;
    private List<Point> runtimeVsDatasetSizeParallel;
    private List<Point> SSEVsDatasetSizeSequential;
    private List<Point> SSEVsDatasetSizeParallel;

    public void runExperiments() {

    }

    public List<Point> getRuntimeVsDatasetSizeSequential() {
        return runtimeVsDatasetSizeSequential;
    }

    public List<Point> getRuntimeVsDatasetSizeParallel() {
        return runtimeVsDatasetSizeParallel;
    }

    public List<Point> getSSEVsDatasetSizeSequential() {
        return SSEVsDatasetSizeSequential;
    }

    public List<Point> getSSEVsDatasetSizeParallel() {
        return SSEVsDatasetSizeParallel;
    }


}
