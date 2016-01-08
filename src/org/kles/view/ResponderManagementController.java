package org.kles.view;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kles.MainApp;
import org.kles.fx.custom.DateTimePicker;
import org.kles.fx.custom.FxUtil;
import org.kles.fx.custom.TextFieldValidator;
import org.kles.model.Responder;
import org.kles.task.GetResponderTask;

/**
 * FXML Controller class
 *
 * @author Jeremy.CHAUT
 */
public class ResponderManagementController {

    @FXML
    private Label labelMessage;
    @FXML
    private TextArea message;
    @FXML
    private DateTimePicker fromPicker, toPicker;
    @FXML
    private Button buttonOK, buttonKO;
    @FXML
    private ProgressIndicator progressResponder;

    private BooleanBinding messageBoolean;
    protected Map<BooleanBinding, String> messages = new LinkedHashMap<>();
    private MainApp mainApp;
    private Service<JSONObject> responderService;
    private Service<JSONObject> deleteService;
    private GetResponderTask taskGetResponder;
    private boolean create = true;

    @FXML
    private void initialize() {
        messageBoolean = TextFieldValidator.emptyTextAreaBinding(message, MainApp.resourceMessage.getString("message.checkpassword"), messages);
        buttonOK.disableProperty().bind(messageBoolean);

        responderService = new Service<JSONObject>() {
            @Override
            protected Task<JSONObject> createTask() {
                return new Task<JSONObject>() {
                    @Override
                    protected JSONObject call() throws ParseException {
/String msg=new String(message.getText().getBytes(), Charset.forName("UTF-8")); 
                        String msg = "";
                        try {
                            msg = new String(message.getText().getBytes("UTF-8"), "ISO-8859-1");
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(ResponderManagementController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (mainApp.getAccountData().getResponder() == null) {
                            create = true;
                            return mainApp.getRestFulOVH().createResponder(msg, fromPicker.getDateTimeValue().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), toPicker.getDateTimeValue().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                        } else {
                            create = false;
                            return mainApp.getRestFulOVH().changeResponder(msg, fromPicker.getDateTimeValue().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), toPicker.getDateTimeValue().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                        }
                    }
                };
            }
        };
        responderService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    progressResponder.setVisible(false);
                    FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                            mainApp.getResourceMessage().getString("error.changeresponder"),
                            responderService.getException().getMessage());
                    taskGetResponder.run();
                    break;
                case CANCELLED:
                    break;
                case SUCCEEDED:
                    progressResponder.setVisible(false);
                    if (!create && responderService.getValue() == null) {
                        labelMessage.setText(mainApp.getResourceMessage().getString("message.successmodifiedresponder"));
                    } else if (!create && responderService.getValue() != null) {
                        FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                                mainApp.getResourceMessage().getString("error.changeresponder"),
                                responderService.getValue().get("message").toString());
                    } else if (create) {
                        if (responderService.getValue() != null) {
                            if (responderService.getValue().get("action") != null) {
                                if (responderService.getValue().get("action").toString().equals("add")) {
                                    labelMessage.setText(mainApp.getResourceMessage().getString("message.successaddedresponder"));
                                    buttonKO.setDisable(false);
                                }
                            } else if (responderService.getValue().get("message") != null) {
                                FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                                        mainApp.getResourceMessage().getString("error.changeresponder"),
                                        responderService.getValue().get("message").toString());
                                buttonKO.setDisable(true);
                            }
                        }
                    }
                    taskGetResponder.run();
                    break;
            }
        });

        deleteService = new Service<JSONObject>() {
            @Override
            protected Task<JSONObject> createTask() {
                return new Task<JSONObject>() {
                    @Override
                    protected JSONObject call() throws ParseException {
                        return mainApp.getRestFulOVH().deleteResponder();
                    }
                };
            }
        };
        deleteService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    progressResponder.setVisible(false);
                    JSONObject json;
                    try {
                        json = (JSONObject) new JSONParser().parse(mainApp.getRestFulOVH().getResponse().toString());
                        if (json.get("message") != null) {
                            FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                                    mainApp.getResourceMessage().getString("error.changeresponder"),
                                    json.get("message").toString());
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(ResponderManagementController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    taskGetResponder.run();
                    buttonKO.setDisable(false);
                    break;
                case CANCELLED:
                    break;
                case SUCCEEDED:
                    progressResponder.setVisible(false);
                    if (deleteService.getValue() != null) {
                        if (deleteService.getValue().get("action") != null) {
                            mainApp.getAccountData().setResponderProperty(null);
                            message.setText("");
//                            fromPicker.setDateTimeValue(ZonedDateTime.now());
//                            toPicker.setDateTimeValue(ZonedDateTime.now().minusWeeks(10));
                            buttonKO.setDisable(true);
                        } else if (deleteService.getValue().get("message") != null) {
                            FxUtil.showAlert(Alert.AlertType.ERROR, mainApp.getResourceMessage().getString("main.error"),
                                    mainApp.getResourceMessage().getString("error.changeresponder"),
                                    deleteService.getValue().get("message").toString());
                            buttonKO.setDisable(false);
                        }
                    }
                    taskGetResponder.run();
                    break;
            }
        });
    }

    @FXML
    public void handleOK() {
        labelMessage.setText("");
        progressResponder.setVisible(true);
        createTask();
        responderService.restart();
    }

    @FXML
    public void handleDelete() {
        labelMessage.setText("");
        progressResponder.setVisible(true);
        createTask();
        deleteService.restart();
    }

    private void createTask() {
        taskGetResponder = new GetResponderTask();
        taskGetResponder.setMainApp(mainApp);
    }

**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        buttonKO.setDisable(true);
        createTask();
        this.mainApp.getAccountData().getResponderProperty().addListener(new ChangeListener<Responder>() {

            @Override
            public void changed(ObservableValue<? extends Responder> observable, Responder oldValue, Responder newValue) {
                if (newValue != null) {
                    create = false;
                    buttonKO.setDisable(false);
                    message.setText(mainApp.getAccountData().getResponder().getContent());
                    fromPicker.setDateTimeValue(mainApp.getAccountData().getResponder().getFromDate());
                    toPicker.setDateTimeValue(mainApp.getAccountData().getResponder().getToDate());
                } else {
                    create = true;
                }
            }
        });
    }
}
