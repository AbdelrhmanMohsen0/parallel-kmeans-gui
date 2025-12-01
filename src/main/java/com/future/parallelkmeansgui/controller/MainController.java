package com.future.parallelkmeansgui.controller;

import atlantafx.base.controls.RingProgressIndicator;
import com.future.parallelkmeansgui.model.Point;
import com.future.parallelkmeansgui.view.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public ComboBox<String> datasetCompoBox;
    public TextField datasetFileTextField;
    public Button browseButton;
    public StackPane loadingPane;
    public Button runExperimentsButton;
    public Button clusterDatasetButton;
    public Spinner<Integer> numOfDataPointsSpinner;
    public HBox syntheticPane;
    public AnchorPane browsePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        browseButton.setOnAction(event -> onBrowse());
        clusterDatasetButton.setOnAction(event -> onCluster());
        runExperimentsButton.setOnAction(event -> onRunExperiments());
        initDatasetCompoBox();
        initLoadingRing();
        initDatasetFileTextField();
        initDatasetPointsSpinner();
    }

    private void initDatasetCompoBox() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Load 2D Dataset",
                "Synthetic 2D Dataset"
        );

        datasetCompoBox.setOnAction(event -> toggleLayout());
        datasetCompoBox.setItems(options);
        datasetCompoBox.getSelectionModel().selectFirst();
    }

    private void initLoadingRing() {
        RingProgressIndicator ring = new RingProgressIndicator();
        ring.setPrefSize(100, 100);
        ring.setMinSize(100, 100);
        ring.progressProperty().bind(Bindings.createDoubleBinding(
                () -> loadingPane.isVisible() ? -1d : 0d,
                loadingPane.visibleProperty()
        ));
        loadingPane.getChildren().add(ring);
    }

    private void initDatasetFileTextField() {
        datasetFileTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (datasetCompoBox.getValue().equals("Synthetic 2D Dataset")) {
                clusterDatasetButton.setDisable(false);
            } else {
                clusterDatasetButton.setDisable(newValue == null || newValue.isEmpty());
            }
        });
    }

    private void initDatasetPointsSpinner() {
        syntheticPane.setVisible(false);
        syntheticPane.setManaged(false);

        int minValue = 10;
        int maxValue = Integer.MAX_VALUE;
        int initialValue = 100000;
        int stepSize = 1000;

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        minValue,
                        maxValue,
                        initialValue,
                        stepSize
                );
        numOfDataPointsSpinner.setValueFactory(valueFactory);

        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                valueFactory.getValue(),
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("-?\\d*")) {
                        return change;
                    }
                    return null;
                });

        numOfDataPointsSpinner.getEditor().setTextFormatter(formatter);

        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
    }

    private void toggleLayout() {
        String dataset = datasetCompoBox.getValue();
        datasetFileTextField.setText("");
        if (dataset.equals("Load 2D Dataset")) {
            datasetFileTextField.setDisable(false);
            browseButton.setDisable(false);
            clusterDatasetButton.setDisable(true);
            syntheticPane.setVisible(false);
            syntheticPane.setManaged(false);
            browsePane.setVisible(true);
            browsePane.setManaged(true);
        } else {
            datasetFileTextField.setDisable(true);
            browseButton.setDisable(true);
            clusterDatasetButton.setDisable(false);
            syntheticPane.setVisible(true);
            syntheticPane.setManaged(true);
            browsePane.setVisible(false);
            browsePane.setManaged(false);
        }
    }

    private void onBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV File");

        FileChooser.ExtensionFilter csvFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");

        fileChooser.getExtensionFilters().add(csvFilter);

        FileChooser.ExtensionFilter allFilter =
                new FileChooser.ExtensionFilter("All Files", "*.*");

        fileChooser.getExtensionFilters().add(allFilter);

        File selectedFile = fileChooser.showOpenDialog(browseButton.getScene().getWindow());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            datasetFileTextField.setText(filePath);
        }
    }

    private void onCluster() {
        switch (datasetCompoBox.getValue()) {
            case "Synthetic 2D Dataset" -> clusterSyntheticDataset();
            case "Load 2D Dataset" -> clusterCSVDataset();
        }
    }

    private void clusterSyntheticDataset() {
//        int numberOfPoints = numOfDataPointsSpinner.getValue();
        List<Node> contentNodes = generateTestGraphs();
        ViewManager.getInstance().showClusterReportWindow(
                "Clustering Report",
                "Some metrics after applying K-Means Clustering algorithm on the specified dataset.",
                contentNodes
        );
    }

    private void clusterCSVDataset() {
//        String datasetPath = datasetFileTextField.getText();

    }

    private void onRunExperiments() {

    }

    private List<Node> generateTestGraphs() {
        GraphView testGraph1 = new ElbowMethodGraph(
                TestPointsGenerator.generateElbowMethodTest(),
                TestPointsGenerator.generateElbowMethodTest(),
                new Point(4, 55),
                new Point(4, 55)
        );
        GraphView testGraph2 = new RuntimeVsKGraph(
          TestPointsGenerator.generateSequentialRuntimeKTest(),
          TestPointsGenerator.generateParallelRuntimeKTest()
        );
        GraphView testGraph3 = new ElbowMethodGraph(
                TestPointsGenerator.generateElbowMethodTest(),
                TestPointsGenerator.generateElbowMethodTest(),
                new Point(4, 55),
                new Point(4, 55)
        );
        return List.of(testGraph1.generateGraph(), testGraph2.generateGraph(), testGraph3.generateGraph());
    }
}
