package org.kles.view;

import org.kles.fx.custom.FxUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.kles.MainApp;

import org.kles.model.AbstractDataModel;

/**
 * Dialog to edit details of a environment.
 *
 * @author Jérémy Chaut
 */
public class AbstractDataModelEditController {

    protected MainApp mainApp;
    protected Stage dialogStage;
    protected AbstractDataModel datamodel;
    protected boolean okClicked = false;
    protected String errorMessage = "";
    protected Map<BooleanBinding, String> messages = new LinkedHashMap<>();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp main) {
        mainApp = main;
    }

    /**
     * Sets the datamodel to be edited in the dialog.
     *
     * @param datamodel
     */
    public void setDataModel(AbstractDataModel datamodel) {
        this.datamodel = datamodel;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    public boolean isInputValid() {
        if (messages.size() > 0) {
            Iterator<Entry<BooleanBinding, String>> it = messages.entrySet().iterator();
            while (it.hasNext()) {
                Entry<BooleanBinding, String> temp = it.next();
                if (temp.getKey().get()) {
                    errorMessage += temp.getValue() + "\n";
                }

            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, MainApp.resourceMessage.getString("main.controlData"), MainApp.resourceMessage.getString("main.invalidData"), errorMessage);
            return false;
        }
    }
}
