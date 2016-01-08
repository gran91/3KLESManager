package org.kles.login;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kles.MainApp;
import org.kles.view.LoginController;
import resources.Resource;

public class LoginManager {

    private final Stage primaryStage, loginStage;
    private final MainApp mainApp;

    public LoginManager(MainApp mainApp, Stage stage) {
        this.mainApp = mainApp;
        primaryStage = stage;
        loginStage = new Stage(StageStyle.UNDECORATED);
    }

    public void authenticated(String sessionID) {
        loginStage.close();
        showMainView();
    }

    public void logout() {
        showLoginScreen();
    }

    public void closeApp() {
        loginStage.close();
        primaryStage.close();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/kles/view/Login.fxml"));
            loader.setResources(ResourceBundle.getBundle("resources.language", Locale.getDefault()));
            AnchorPane loginPane = loader.load();
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.initOwner(primaryStage);
            loginStage.getIcons().add(Resource.LOGO_ICON);
            Scene scene = new Scene(loginPane);
            scene.getStylesheets().add(getClass().getResource("/org/kles/css/application.css").toExternalForm());
            loginStage.setScene(scene);
            LoginController controller = loader.getController();
            controller.initManager(this);
            controller.setMainApp(mainApp);
            controller.setDialogStage(loginStage);
            loginStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMainView() {
        primaryStage.show();
    }
}
