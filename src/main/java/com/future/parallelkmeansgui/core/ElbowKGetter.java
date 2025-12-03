package com.future.parallelkmeansgui.core;

import com.future.parallelkmeansgui.model.Result;

import java.util.List;

public class ElbowKGetter {

    public static Result getBestResult(List<Result> results) {
        int firstResultK = results.getFirst().clusters().size();
        double firstResultSSE = results.getFirst().sse();
        return null;
    }
}
