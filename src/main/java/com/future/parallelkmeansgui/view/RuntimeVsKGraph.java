package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class RuntimeVsKGraph extends GraphView {

    private static final int MIN_K = 1;
    private static final int MAX_K = 20;
    private static final int STEP_K = 1;

    private static final double MIN_RUNTIME = 0;
    private final double MAX_RUNTIME;
    private final double STEP_RUNTIME;

    private final List<Point> runtimeVsKPointsSequential;
    private final List<Point> runtimeVsKPointsParallel;

    public RuntimeVsKGraph(List<Point> runtimeVsKPointsSequential, List<Point> runtimeVsKPointsParallel) {
        this.runtimeVsKPointsSequential = runtimeVsKPointsSequential;
        this.runtimeVsKPointsParallel = runtimeVsKPointsParallel;

        long maxRuntime = 0;
        for (Point p : runtimeVsKPointsSequential) {
            if (p.y() > maxRuntime) {
                maxRuntime = (long) p.y();
            }
        }
        for (Point p : runtimeVsKPointsParallel) {
            if (p.y() > maxRuntime) {
                maxRuntime = (long) p.y();
            }
        }

        this.MAX_RUNTIME = roundUp(maxRuntime);
        this.STEP_RUNTIME = MAX_RUNTIME / 10;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(MIN_K, MAX_K, STEP_K);
        x.setLabel("K");

        var y = new NumberAxis(MIN_RUNTIME, MAX_RUNTIME, STEP_RUNTIME);
        y.setLabel("Runtime (μs)");

        var series1 = creatSeries("Sequential Algorithm", runtimeVsKPointsSequential);
        var series2 = creatSeries("Parallel Algorithm", runtimeVsKPointsParallel);

        var chart = new LineChart<>(x, y);
        chart.setTitle("Runtime vs K (K-means)");
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
