package com.future.parallelkmeansgui.util;

import com.future.parallelkmeansgui.model.Point;

public class DistanceCalculator {

    public static double calculate(Point p1, Point p2) {
        double sum = Math.pow(p1.x() - p2.x(), 2) + Math.pow(p1.y() - p2.y(), 2);
        return Math.sqrt(sum);
    }
}
