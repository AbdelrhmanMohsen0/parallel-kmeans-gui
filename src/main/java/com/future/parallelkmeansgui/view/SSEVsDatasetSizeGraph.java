package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class SSEVsDatasetSizeGraph extends GraphView {

    private static final int MIN_DatasetSize = 0;
    private static final int MAX_DatasetSize = 2_000_000;
    private static final int STEP_DatasetSize = 200_000;

    private static final double MIN_SSE = 0.0;
    private final double MAX_SSE;
    private final double STEP_SSE;

    private final List<Point> pointsSequential;
    private final List<Point> pointsParallel;

    public SSEVsDatasetSizeGraph(List<Point> pointsSequential, List<Point> pointsParallel) {
        this.pointsSequential = pointsSequential;
        this.pointsParallel = pointsParallel;

        long maxSSE = 0;
        for (Point p : pointsSequential) {
            if (p.y() > maxSSE) {
                maxSSE = (long) p.y();
            }
        }
        for (Point p : pointsParallel) {
            if (p.y() > maxSSE) {
                maxSSE = (long) p.y();
            }
        }

        this.MAX_SSE = roundUp(maxSSE);
        this.STEP_SSE = MAX_SSE / 10;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(MIN_DatasetSize, MAX_DatasetSize, STEP_DatasetSize);
        x.setLabel("Dataset Size");

        var y = new NumberAxis(MIN_SSE, MAX_SSE, STEP_SSE);
        y.setLabel("SSE");

        var series1 = creatSeries("Sequential Algorithm", pointsSequential);
        var series2 = creatSeries("Parallel Algorithm", pointsParallel);

        var chart = new LineChart<>(x, y);
        chart.setTitle("SSE vs Dataset Size");
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
