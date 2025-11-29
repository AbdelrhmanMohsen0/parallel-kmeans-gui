package com.future.parallelkmeansgui.util;

public class RuntimeTimer {

    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public long stop() {
        return System.nanoTime() - startTime;
    }

}
