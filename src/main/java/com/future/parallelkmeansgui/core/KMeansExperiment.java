package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Experiment;

import java.util.HashMap;
import java.util.Map;

public class KMeansExperiment implements Runnable {

    // The key is K and the value is the experiment corresponding to this K
    private final Map<Integer, Experiment> sequentialExperimentsByK = new HashMap<>();
    private final Map<Integer, Experiment> parallelExperimentsByK = new HashMap<>();

    // The experiment with the lowest SSE according to the elbow Method
    private Experiment bestSequentialExperiment;
    private Experiment bestParallelExperiment;

    // The key is the dataset size and the value is the experiment corresponding to it with the K fixed to the best K
    private final Map<Long, Experiment> sequentialExperimentsByDatasetSize = new HashMap<>();
    private final Map<Long, Experiment> parallelExperimentsByDatasetSize = new HashMap<>();



    // Implement KMeansExperiment to run sequential and parallel K-means for several values of K and dataset sizes.
    @Override
    public void run() {

    }

    public Map<Integer, Experiment> getSequentialExperiments() {
        return sequentialExperimentsByK;
    }

    public Map<Integer, Experiment> getParallelExperiments() {
        return parallelExperimentsByK;
    }
}
