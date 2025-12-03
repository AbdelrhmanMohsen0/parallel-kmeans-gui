package com.future.parallelkmeansgui.model;

import java.util.List;

public record Result(List<Cluster> clusters, long runtime, double sse, int k) {
}
