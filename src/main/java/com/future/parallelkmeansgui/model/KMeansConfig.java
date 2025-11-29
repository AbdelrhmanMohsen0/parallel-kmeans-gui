package com.future.parallelkmeansgui.model;

import java.util.List;

public record KMeansConfig(List<Point> points, int k, long maxIterations, double tolerance) {
}
