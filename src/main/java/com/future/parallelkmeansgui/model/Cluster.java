package com.future.parallelkmeansgui.model;

import java.util.List;

public record Cluster(Point centroid, List<Point> points) {
}
