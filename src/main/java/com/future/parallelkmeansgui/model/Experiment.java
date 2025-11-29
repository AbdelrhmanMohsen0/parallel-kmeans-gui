package com.future.parallelkmeansgui.model;

import java.util.List;

public record Experiment(List<Cluster> clusters, long runtime, double sse) {
}
