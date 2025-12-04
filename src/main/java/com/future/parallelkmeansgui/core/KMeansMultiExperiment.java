package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.KMeansConfig;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.model.Result;
import com.future.parallelkmeansgui.util.DatasetGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.future.parallelkmeansgui.core.KMeansExperiment.MAX_ITERATION;
import static com.future.parallelkmeansgui.core.KMeansExperiment.TOLERANCE;

public class KMeansMultiExperiment {

    public static int MAX_DATASET_SIZE = 2_001_000;
    public static int DATASET_SIZE_STEP = 100_000;
    public static int INITIAL_DATASET_SIZE = 1000;
    public static int K = 3;

    private final List<Point> runtimeVsDatasetSizeSequential = new ArrayList<>();
    private final List<Point> runtimeVsDatasetSizeParallel = new ArrayList<>();
    private final List<Point> SSEVsDatasetSizeSequential  = new ArrayList<>();
    private final List<Point> SSEVsDatasetSizeParallel = new ArrayList<>();

    public void runExperiments() {
        List<Result> sequentialResults = getSequentialResults();
        List<Result> parallelResults = getParallelResults();

        for (int i = INITIAL_DATASET_SIZE, j = 0; i <= MAX_DATASET_SIZE; i += DATASET_SIZE_STEP, j++) {
            runtimeVsDatasetSizeSequential.add(new Point(i, sequentialResults.get(j).runtime()));
            runtimeVsDatasetSizeParallel.add(new Point(i, parallelResults.get(j).runtime()));
            SSEVsDatasetSizeSequential.add(new Point(i, sequentialResults.get(j).sse()));
            SSEVsDatasetSizeParallel.add(new Point(i, parallelResults.get(j).sse()));
        }
    }

    private List<Result> getSequentialResults() {
        List<Result> results = new ArrayList<>();
        for (int i = INITIAL_DATASET_SIZE; i <= MAX_DATASET_SIZE; i += DATASET_SIZE_STEP) {
            List<Point> dataset = DatasetGenerator.generate2D(i);
            KMeans kmeans = new KMeansSequentialImpl(new KMeansConfig(dataset, K, MAX_ITERATION, TOLERANCE));
            Result result = kmeans.runKMeans();
            results.add(result);
        }
        return results;
    }

    private List<Result> getParallelResults() {
        List<Result> results = new ArrayList<>();
        for (int i = INITIAL_DATASET_SIZE; i <= MAX_DATASET_SIZE; i += DATASET_SIZE_STEP) {
            List<Point> dataset = DatasetGenerator.generate2D(i);
            KMeans kmeans = new KMeansParallelImpl(new KMeansConfig(dataset, K, MAX_ITERATION, TOLERANCE));
            Result result = kmeans.runKMeans();
            results.add(result);
        }
        return results;
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
