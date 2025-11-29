package com.future.parallelkmeansgui.util;

import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatasetGenerator {

    private final Random rand = new Random();

    public List<Point> generate2D(long numPoints) {
        List<Point> points = new ArrayList<>();
        for (long i = 0; i < numPoints; i++) {
            points.add(new Point(rand.nextDouble(), rand.nextDouble()));
        }
        return points;
    }
}
