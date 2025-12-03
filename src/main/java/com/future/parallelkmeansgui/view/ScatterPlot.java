package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Cluster;
import com.future.parallelkmeansgui.model.Point;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class ScatterPlot extends GraphView {

    private final List<Cluster> clusters;
    private final String title;
    private static final List<Color> CLUSTER_COLORS = List.of(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.ORANGE,
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW,
            Color.BLACK,
            Color.BROWN,
            Color.PURPLE,
            Color.DARKGREEN,
            Color.DARKORANGE,
            Color.DARKMAGENTA,
            Color.DEEPPINK,
            Color.DODGERBLUE,
            Color.GOLD,
            Color.TOMATO,
            Color.SPRINGGREEN,
            Color.INDIGO,
            Color.TURQUOISE
    );

    public ScatterPlot(List<Cluster> clusters, String title) {
        this.clusters = clusters;
        this.title = title;
    }

    @Override
    public Node generateGraph() {
        var x = new NumberAxis(0.0, 1.0, 0.1);
        x.setLabel("X");
        var y = new NumberAxis(0.0, 1.0, 0.1);
        y.setLabel("Y");

        var centroids = new XYChart.Series<Number, Number>();
        centroids.setName("Centroids");
        List<XYChart.Series<Number, Number>> clustersSeries = createClustersSeries(centroids);

        var chart = new ScatterChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setTitle(title);
        chart.setMinHeight(300);
        chart.getData().addAll(clustersSeries);
        chart.getData().add(centroids);
        return chart;
    }

    private List<XYChart.Series<Number, Number>> createClustersSeries(XYChart.Series<Number, Number> centroids) {
        List<XYChart.Series<Number, Number>> clustersSeries = new ArrayList<>();
        int currentColorIndex = 0;
        for (Cluster cluster : clusters) {
            XYChart.Data<Number, Number> centroid = new XYChart.Data<>(cluster.centroid().x(), cluster.centroid().y());
            centroid.setNode(createXShape());
            centroids.getData().add(centroid);

            var series = new XYChart.Series<Number, Number>();
            for (Point point : cluster.points()) {
                XYChart.Data<Number, Number> data = new XYChart.Data<>(point.x(), point.y());
                data.setNode(new Circle(3, CLUSTER_COLORS.get(currentColorIndex)));
                series.getData().add(data);
            }
            currentColorIndex = (currentColorIndex + 1) % CLUSTER_COLORS.size();
            clustersSeries.add(series);

        }
        return clustersSeries;
    }

    private Group createXShape() {
        double size = 10;
        Line l1 = new Line(-size/2, -size/2, size/2, size/2);
        Line l2 = new Line(-size/2, size/2, size/2, -size/2);

        l1.setStroke(Color.BLACK);
        l2.setStroke(Color.BLACK);
        l1.setStrokeWidth(2);
        l2.setStrokeWidth(2);

        return new Group(l1, l2);
    }
}
