package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ElbowMethodGraph extends GraphView {

    private static final int MIN_K = 1;
    private static final int MAX_K = 20;
    private static final int STEP_K = 1;

    private static final double MIN_SSE = 0.0;
    private static final double MAX_SSE = 200.0;
    private static final double STEP_SSE = 20.0;

    private final List<Point> SSEvsKPointsSequential;
    private final List<Point> SSEvsKPointsParallel;
    private final Point optimalSequentialKPoint;
    private final Point optimalParallelKPoint;

    public ElbowMethodGraph(List<Point> SSEvsKPointsSequential, List<Point> SSEvsKPointsParallel, Point optimalKPoint, Point optimalParallelKPoint) {
        this.SSEvsKPointsSequential = SSEvsKPointsSequential;
        this.SSEvsKPointsParallel = SSEvsKPointsParallel;
        this.optimalSequentialKPoint = optimalKPoint;
        this.optimalParallelKPoint = optimalParallelKPoint;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(MIN_K, MAX_K, STEP_K);
        x.setLabel("K");

        var y = new NumberAxis(MIN_SSE, MAX_SSE, STEP_SSE);
        y.setLabel("SSE");

        var series1 = creatSeries("Sequential Algorithm", SSEvsKPointsSequential);
        var series2 = creatSeries("Parallel Algorithm", SSEvsKPointsParallel);

        var chart = new LineChart<>(x, y);
        chart.setTitle("SSE vs K (Elbow Method)");
        chart.setMinHeight(300);
        chart.getData().add(series1);
        chart.getData().add(series2);

        addOptimalKPoints(series1, series2);

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

    private void addOptimalKPoints(XYChart.Series<Number, Number> series1, XYChart.Series<Number, Number> series2) {
        XYChart.Data<Number, Number> bestKSequential = new XYChart.Data<>(optimalSequentialKPoint.x(), optimalSequentialKPoint.y());
        XYChart.Data<Number, Number> bestKParallel = new XYChart.Data<>(optimalParallelKPoint.x(), optimalParallelKPoint.y());
        Platform.runLater(() -> {
            bestKSequential.getNode().setStyle("-fx-background-color: #0067a2; -fx-padding: 6px;");
            bestKParallel.getNode().setStyle("-fx-background-color: #0067a2; -fx-padding: 6px;");
        });
        series1.getData().add(bestKSequential);
        series2.getData().add(bestKParallel);
    }
}
