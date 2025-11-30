package com.future.parallelkmeansgui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewManager {

    private static ViewManager viewManager;
    private ViewManager(){}
    public static ViewManager getInstance() {
        if (viewManager == null)
            viewManager = new ViewManager();
        return viewManager;
    }

    public void showClusterReportWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report-view.fxml"));
        createStage(loader,"Clustering Report", 800, 650);
    }

    private void createStage(FXMLLoader loader, String title, int width, int height) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), width, height);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinWidth(width*0.85);
        stage.setMinHeight(height*0.85);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}
