module com.future.parallelkmeansgui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.future.parallelkmeansgui to javafx.fxml;
    exports com.future.parallelkmeansgui;
    exports com.future.parallelkmeansgui.controller;
    exports com.future.parallelkmeansgui.core;
    exports com.future.parallelkmeansgui.model;
    exports com.future.parallelkmeansgui.util;
}