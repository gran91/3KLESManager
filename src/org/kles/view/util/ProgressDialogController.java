package org.kles.view.util;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a division.
 *
 * @author Marco Jakob
 */
public class ProgressDialogController {

    @FXML
    private Label label;
    @FXML
    private ProgressIndicator progress;

    private Stage stage;

    private Service service;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    public void hide() {
        stage.hide();
    }

    public void close() {
        stage.close();
    }

    public void show() {
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Label getLabel() {
        return label;
    }

    public ProgressIndicator getProgress() {
        return progress;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
        label.textProperty().bind(service.messageProperty());
    }
}
