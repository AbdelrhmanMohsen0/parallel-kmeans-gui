package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class RuntimeVsDatasetSizeGraph extends GraphView {

    private static final int MIN_DatasetSize = 0;
    private static final int MAX_DatasetSize = 1_000_000;
    private static final int STEP_DatasetSize = 100_000;

    private static final double MIN_RUNTIME = 0.0;
    private static final double MAX_RUNTIME = 600.0;
    private static final double STEP_RUNTIME = 50.0;

    private final List<Point> pointsSequential;
    private final List<Point> pointsParallel;

    public RuntimeVsDatasetSizeGraph(List<Point> pointsSequential, List<Point> pointsParallel) {
        this.pointsSequential = pointsSequential;
        this.pointsParallel = pointsParallel;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(MIN_DatasetSize, MAX_DatasetSize, STEP_DatasetSize);
        x.setLabel("Dataset Size");

        var y = new NumberAxis(MIN_RUNTIME, MAX_RUNTIME, STEP_RUNTIME);
        y.setLabel("Runtime (ms)");

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
