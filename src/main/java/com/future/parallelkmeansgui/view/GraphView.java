package com.future.parallelkmeansgui.view;

import javafx.scene.Node;

public abstract class GraphView {

    public abstract Node generateGraph();

    protected static long roundUp(long value) {
        if (value <= 0) return 0;
        String s = String.valueOf(value);
        int len = s.length();
        int left = Character.getNumericValue(s.charAt(0)) + 1;

        if (left == 10) {
            return Long.parseLong("1" + "0".repeat(len));
        }

        String sb = left + "0".repeat(len - 1);
        return Long.parseLong(sb);
    }

}
