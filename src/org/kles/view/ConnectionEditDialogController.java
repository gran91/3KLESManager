package org.kles.view;

import org.kles.fx.custom.TextFieldValidator;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.kles.MainApp;
import org.kles.model.AbstractDataModel;

/**
 * Dialog to edit details of a developer.
 *
 * @author Jérémy Chaut
 */
public class ConnectionEditDialogController extends AbstractDataModelEditController {

    @FXML
    private TextField urlField;
    @FXML
    private TextField portField;

    private BooleanBinding urlM3Boolean, portBoolean;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        urlM3Boolean = TextFieldValidator.patternTextFieldBinding(urlField, TextFieldValidator.hostnamePattern, MainApp.resourceMessage.getString("message.url"), messages);
        portBoolean = TextFieldValidator.patternTextFieldBinding(portField, TextFieldValidator.allPortNumberPattern, MainApp.resourceMessage.getString("message.port"), messages);
        BooleanBinding[] mandotariesBinding = new BooleanBinding[]{urlM3Boolean, portBoolean};
        BooleanBinding mandatoryBinding = TextFieldValidator.any(mandotariesBinding);
        LongBinding nbMandatoryBinding = TextFieldValidator.count(mandotariesBinding);

        urlField.setText((MainApp.prefs.get("connection.url", null) == null) ? "ssl0.ovh.net" : MainApp.prefs.get("connection.url", null));
        portField.setText((MainApp.prefs.get("connection.port", null) == null) ? "993" : MainApp.prefs.get("connection.port", null));
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            MainApp.prefs.put("connection.url", urlField.getText());
            MainApp.prefs.put("connection.port", portField.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    @Override
    public boolean isInputValid() {
        errorMessage = "";
        return super.isInputValid();
    }
}
