package org.kles.view;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kles.fx.custom.TextFieldValidator;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.kles.MainApp;
import org.kles.fx.custom.DateTimePicker;
import org.kles.login.LoginManager;
import org.kles.task.GetResponderTask;

/**
 * Dialog to edit details of a developer.
 *
 * @author Jérémy Chaut
 */
public class LoginController extends AbstractDataModelEditController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label labelError;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button buttonOK;

    private BooleanBinding loginBoolean, passwordBoolean;
    private LoginManager loginManager;
    private Service<String> connectionService;
    private String sessionID = null;
    private DateTimeFormatter ovhFormatter = DateTimeFormatter.ofPattern(DateTimePicker.stdformat);
    private int inMail = 0;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        loginBoolean = TextFieldValidator.patternTextFieldBinding(loginField, TextFieldValidator.emailPattern, MainApp.resourceMessage.getString("message.email"), messages);
        passwordBoolean = TextFieldValidator.emptyTextFieldBinding(passwordField, MainApp.resourceMessage.getString("message.password"), messages);
        BooleanBinding[] mandotariesBinding = new BooleanBinding[]{loginBoolean, passwordBoolean};
        BooleanBinding mandatoryBinding = TextFieldValidator.any(mandotariesBinding);
        LongBinding nbMandatoryBinding = TextFieldValidator.count(mandotariesBinding);

        connectionService = new Service<String>() {

            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws NoSuchProviderException, MessagingException {
                        Session session = Session.getDefaultInstance(System.getProperties());
                        Store store = session.getStore("imaps");
                        String host = (MainApp.prefs.get("connection.url", null) == null) ? "ssl0.ovh.net" : MainApp.prefs.get("connection.url", null);
                        int port = (MainApp.prefs.get("connection.port", null) == null) ? 993 : Integer.parseInt(MainApp.prefs.get("connection.port", null));
                        store.connect(host, port, loginField.getText(), passwordField.getText());
                        Folder inbox = store.getFolder("INBOX");
                        inMail = inbox.getMessageCount();
                        for (Folder f : inbox.list()) {
                            System.out.println(f.getName());
                        }
                        inbox.open(Folder.READ_ONLY);
                        return generateSessionID();
                    }
                };
            }
        };
        connectionService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                    labelError.setTextFill(Color.RED);
                    labelError.setText(connectionService.getException().getMessage());
                    progress.setVisible(false);
                    sessionID = null;
                    buttonOK.setDisable(false);
                    break;
                case CANCELLED:
                case SUCCEEDED:
                    sessionID = connectionService.getValue();
                    progress.setVisible(false);
                    if (sessionID != null) {
                        MainApp.prefs.put("login", loginField.getText());
                        MainApp.prefs.put("password", passwordField.getText());
                        mainApp.initRestFulOVH(loginField.getText().split("@")[0]);
                        try {
                            mainApp.getAccountData().setEmail(mainApp.getRestFulOVH().getAccount());
                            JSONObject obj = mainApp.getRestFulOVH().getAccountInformation();
                            JSONObject usg = mainApp.getRestFulOVH().getAccountUsage();
                            mainApp.getAccountData().setEmail(obj.get("accountName").toString());
                            mainApp.getAccountData().setName(obj.get("description").toString());
                            mainApp.getAccountData().setSize(Long.parseLong(obj.get("size").toString()));
                            mainApp.getAccountData().setQuota(Long.parseLong(usg.get("quota").toString()));
                            mainApp.getAccountData().setCountEmail(Integer.parseInt(usg.get("emailCount").toString()));
                            mainApp.getAccountData().setInEmail(inMail);

                            GetResponderTask task = new GetResponderTask();
                            task.setMainApp(mainApp);
                            task.run();

//JSONObject resp = mainApp.getRestFulOVH().getResponder();
//                            JSONObject resp=task.getValue();
//                            if (resp != null) {
//                                Responder respond = new Responder();
//                                respond.setContent(resp.get("content").toString());
//                                respond.setCopy(Boolean.getBoolean(resp.get("copy").toString()));
//                                ZonedDateTime from=ZonedDateTime.parse(resp.get("from").toString());
//                                ZonedDateTime to=ZonedDateTime.parse(resp.get("to").toString());
//                                respond.setFromDate(from);
//                                respond.setToDate(to);
//                                mainApp.getAccountData().getResponderProperty().set(respond);
//                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        loginManager.authenticated(sessionID);
                        okClicked = true;
                    }
                    buttonOK.setDisable(false);
                    break;
            }
        });
    }

    public void initManager(final LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    private static int ssID = 0;

    private String generateSessionID() {
        ssID++;
        return "3klessession " + ssID;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            progress.setVisible(true);
            buttonOK.setDisable(true);
            labelError.setTextFill(Color.BLACK);
            labelError.setText(mainApp.getResourceMessage().getString("main.connecting"));
            connectionService.restart();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        loginManager.closeApp();
    }

    @FXML
    private void handleConnection() {
        mainApp.showConnectionEditDialog();
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

    public void setMainApp(MainApp main) {
        mainApp = main;
        loginField.setText((MainApp.prefs.get("login", null) == null) ? "" : MainApp.prefs.get("login", null));
        passwordField.setText((MainApp.prefs.get("password", null) == null) ? "" : MainApp.prefs.get("password", null));
    }

}
