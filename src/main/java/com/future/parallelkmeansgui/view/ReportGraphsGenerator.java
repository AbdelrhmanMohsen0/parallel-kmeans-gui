package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.core.KMeansExperiment;
import com.future.parallelkmeansgui.core.KMeansMultiExperiment;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.util.DatasetGenerator;
import com.future.parallelkmeansgui.util.DatasetLoader;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.function.Consumer;

public class ReportGraphsGenerator {

    public StackPane loadingPane;
    public final boolean runWithRestarts;

    public ReportGraphsGenerator(StackPane loadingPane, boolean runWithRestarts) {
        this.loadingPane = loadingPane;
        this.runWithRestarts = runWithRestarts;
    }

    public void generateExperimentGraphs(List<Point> dataset, Consumer<List<Node>> onFinished) {
        KMeansExperiment kmeansExperiment = new KMeansExperiment(dataset, runWithRestarts);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                kmeansExperiment.runExperiments();
                return null;
            }
        };

        task.setOnRunning(e -> loadingPane.setVisible(true));
        task.setOnSucceeded(e -> {
            loadingPane.setVisible(false);
            onFinished.accept(generateGraphs(kmeansExperiment));
        });
        task.setOnFailed(e -> loadingPane.setVisible(false));

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void generateExperimentGraphs(int numberOfPoints, Consumer<List<Node>> onFinished) {
        List<Point> dataset = DatasetGenerator.generate2D(numberOfPoints);
        generateExperimentGraphs(dataset, onFinished);
    }

    public void generateExperimentGraphs(String datasetPath, Consumer<List<Node>> onFinished) {
        List<Point> dataset = DatasetLoader.load2DDataset(datasetPath);
        generateExperimentGraphs(dataset, onFinished);
    }

    public void generateMultiExperimentGraphs(Consumer<List<Node>> onFinished) {
        KMeansMultiExperiment kMeansMultiExperiment = new KMeansMultiExperiment();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                kMeansMultiExperiment.runExperiments();
                return null;
            }
        };

        task.setOnRunning(e -> loadingPane.setVisible(true));
        task.setOnSucceeded(e -> {
            loadingPane.setVisible(false);
            onFinished.accept(generateMultiGraphs(kMeansMultiExperiment));
        });
        task.setOnFailed(e -> loadingPane.setVisible(false));

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private List<Node> generateGraphs(KMeansExperiment kmeansExperiment) {
        GraphView SSEVsK = new ElbowMethodGraph(
                kmeansExperiment.getSseVsKSequential(),
                kmeansExperiment.getSseVsKParallel(),
                kmeansExperiment.getBestSSEVsKPointSequential(),
                kmeansExperiment.getBestSSEVsKPointParallel()
        );
        GraphView scatterPlotSequential = new ScatterPlot(
                kmeansExperiment.getBestSequentialClusters(),
                "Scatter Plot For " + kmeansExperiment.getBestSequentialClusters().size() + " Clusters (Sequential)"
        );
        GraphView scatterPlotParallel = new ScatterPlot(
                kmeansExperiment.getBestParallelClusters(),
                "Scatter Plot For " + kmeansExperiment.getBestParallelClusters().size() + " Clusters (Parallel)"
        );
        GraphView runtimeVsK = new RuntimeVsKGraph(
                kmeansExperiment.getRuntimeVsKSequential(),
                kmeansExperiment.getRuntimeVsKParallel()
        );

        return List.of(SSEVsK.generateGraph(),
                scatterPlotSequential.generateGraph(),
                scatterPlotParallel.generateGraph(),
                runtimeVsK.generateGraph());
    }

    private List<Node> generateMultiGraphs(KMeansMultiExperiment kmeansExperiment) {
        GraphView runtimeVsDatasetSize = new RuntimeVsDatasetSizeGraph(
                kmeansExperiment.getRuntimeVsDatasetSizeSequential(),
                kmeansExperiment.getRuntimeVsDatasetSizeParallel()
        );

        GraphView SSEVsDatasetSize = new SSEVsDatasetSizeGraph(
                kmeansExperiment.getSSEVsDatasetSizeSequential(),
                kmeansExperiment.getSSEVsDatasetSizeParallel()
        );
        return List.of(runtimeVsDatasetSize.generateGraph(),
                SSEVsDatasetSize.generateGraph());
    }

}
