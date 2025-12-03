package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Result;

import java.util.Comparator;
import java.util.List;

public class ElbowKGetter {

    public static Result getBestResult(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }

        results.sort(Comparator.comparingInt(r -> r.clusters().size()));

        int n = results.size();

        if (n < 3) {
            return results.get(n - 1);
        }

        Result first = results.get(0);
        Result last = results.get(n - 1);

        double x1 = first.clusters().size();
        double y1 = first.sse();
        double x2 = last.clusters().size();
        double y2 = last.sse();

        double maxDist = -1.0;
        Result bestResult = first;

        for (Result current : results) {
            double x0 = current.clusters().size();
            double y0 = current.sse();

            double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
            double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

            double distance = numerator / denominator;

            if (distance > maxDist) {
                maxDist = distance;
                bestResult = current;
            }
        }

        return bestResult;
    }
}