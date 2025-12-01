package com.future.parallelkmeansgui.view;

import com.future.parallelkmeansgui.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestPointsGenerator {


    public static List<Point> generateElbowMethodTest() {
        List<Point> points = new ArrayList<>();
        Random random = new Random();

        double baseSSE = 200.0;
        for (int k = 1; k <= 20; k++) {
            double sse = baseSSE / k + random.nextDouble() * 10;
            points.add(new Point(k, sse));
        }

        return points;
    }
}
