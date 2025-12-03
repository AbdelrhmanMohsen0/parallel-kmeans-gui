package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class RuntimeVsDatasetSizeGraph extends GraphView {

    private static final int MIN_DATASET_SIZE = 0;
    private static final int MAX_DATASET_SIZE = 1_000_000;
    private static final int STEP_DATASET_SIZE = 100_000;

    private static final double MIN_RUNTIME = 0;
    private final double MAX_RUNTIME;
    private final double STEP_RUNTIME;

    private final List<Point> pointsSequential;
    private final List<Point> pointsParallel;

    public RuntimeVsDatasetSizeGraph(List<Point> pointsSequential, List<Point> pointsParallel) {
        this.pointsSequential = pointsSequential;
        this.pointsParallel = pointsParallel;

        long maxRuntime = 0;
        for (Point p : pointsSequential) {
            if (p.y() > maxRuntime) {
                maxRuntime = (long) p.y();
            }
        }
        for (Point p : pointsParallel) {
            if (p.y() > maxRuntime) {
                maxRuntime = (long) p.y();
            }
        }

        this.MAX_RUNTIME = roundUp(maxRuntime);
        this.STEP_RUNTIME = MAX_RUNTIME / 10;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(MIN_DATASET_SIZE, MAX_DATASET_SIZE, STEP_DATASET_SIZE);
        x.setLabel("Dataset Size");

        var y = new NumberAxis(MIN_RUNTIME, MAX_RUNTIME, STEP_RUNTIME);
        y.setLabel("Runtime (μs)");

        var series1 = creatSeries("Sequential Algorithm", pointsSequential);
        var series2 = creatSeries("Parallel Algorithm", pointsParallel);

        var chart = new LineChart<>(x, y);
        chart.setTitle("Runtime vs Dataset Size");
        chart.setMinHeight(300);
        chart.getData().add(series1);
        chart.getData().add(series2);

        return chart;
    }

    private XYChart.Series<Number, Number> creatSeries(String name, List<Point> points) {
        var series = new XYChart.Series<Number, Number>();
        series.setName(name);
        points.forEach(point -> series.getData().add(
                new XYChart.Data<>(
                        point.x(),
                        point.y()
                )
        ));
        return series;
    }
}
