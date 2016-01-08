package org.kles.view;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javax.mail.Message;
import javax.mail.MessagingException;
import mail.GmailTLS;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kles.MainApp;
import org.kles.fx.custom.FxUtil;
import org.kles.fx.custom.TextFieldValidator;

/**
 * FXML Controller class
 *
 * @author Jeremy.CHAUT
 */
public class ChangePasswordController {

    @FXML
    private Label labelMessage;
    @FXML
    private TextField textPassword;
    @FXML
    private Button buttonPassword;
    @FXML
    private ProgressIndicator progressPassword;

    private BooleanBinding passwordBoolean;
    protected Map<BooleanBinding, String> messages = new LinkedHashMap<>();
    private MainApp mainApp;
    private Service<Void> passwordService;

    @FXML
    private void initialize() {
        passwordBoolean = TextFieldValidator.patternTextFieldBinding(textPassword, TextFieldValidator.ovhPassword, MainApp.resourceMessage.getString("message.checkpassword"), messages);
        buttonPassword.disableProperty().bind(passwordBoolean);
        passwordService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        mainApp.getRestFulOVH().changePassword(textPassword.getText());
                        GmailTLS mail = new GmailTLS();
                        Message msg;
                        try {
                            msg = mail.writeMail("postmaster@3kles-consulting.com", "ChangePassword", textPassword.getText());
                            mail.sendMail(msg);
                        } catch (MessagingException ex) {
                            Logger.getLogger(ChangePasswordController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };
            }
        };
        passwordService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    progressPassword.setVisible(false);
                    break;
                case CANCELLED:
                case SUCCEEDED:
                    progressPassword.setVisible(false);
                    if (mainApp.getRestFulOVH().getResponseCode() == 200) {
                        labelMessage.setText(mainApp.getResourceMessage().getString("message.successpassword"));
                    } else {
                        try {
                            JSONObject error = (JSONObject) new JSONParser().parse(mainApp.getRestFulOVH().getResponse().toString());
                            FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                                    mainApp.getResourceMessage().getString("error.changepassword"),
                                    error.get("message").toString());
                        } catch (ParseException ex) {
                            Logger.getLogger(ChangePasswordController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
            }
        });
    }

    @FXML
    public void handleOK() {
        labelMessage.setText("");
        progressPassword.setVisible(true);
        passwordService.restart();
    }

**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
