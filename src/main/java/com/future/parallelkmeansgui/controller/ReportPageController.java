package com.future.parallelkmeansgui.controller;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportPageController implements Initializable {

    public StackPane content;
    public Button nextButton;
    public Button backButton;
    public Label titleLabel;
    public Label paragraphLabel;

    private final ObjectProperty<Node> currentContent = new SimpleObjectProperty<>(null);
    private final List<Node> contentNodes;

    public ReportPageController(String title, String paragraph, List<Node> contentNodes) {
        this.contentNodes = contentNodes;
        Platform.runLater(() -> {
            titleLabel.setText(title);
            paragraphLabel.setText(paragraph);
        });
        currentContent.set(contentNodes.getFirst());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContentViewer();
        nextButton.setOnAction(event -> onNext());
        backButton.setOnAction(event -> onBack());
    }

    private void onNext() {
        currentContent.set(contentNodes.get(contentNodes.indexOf(currentContent.get()) + 1));
    }

    private void onBack() {
        currentContent.set(contentNodes.get(contentNodes.indexOf(currentContent.get()) - 1));
    }

    private void initContentViewer() {
        content.getChildren().clear();
        content.getChildren().add(currentContent.get());
        backButton.setDisable(contentNodes.indexOf(currentContent.get()) == 0);
        nextButton.setDisable(contentNodes.indexOf(currentContent.get()) == contentNodes.size() - 1);
        currentContent.addListener((observable, oldValue, newValue) -> {
            if (newValue == null || contentNodes.isEmpty()) {
                return;
            }
            backButton.setDisable(contentNodes.indexOf(newValue) == 0);
            nextButton.setDisable(contentNodes.indexOf(newValue) == contentNodes.size() - 1);
            content.getChildren().clear();
            content.getChildren().add(newValue);
        });
    }

}
