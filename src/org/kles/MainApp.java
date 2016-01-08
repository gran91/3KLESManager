package org.kles;

import org.kles.fx.custom.DigitalClock;
import insidefx.undecorator.Undecorator;
import insidefx.undecorator.UndecoratorScene;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.kles.login.LoginManager;
import org.kles.model.AbstractDataModel;

import org.kles.model.Account;
import org.kles.restful.RestFulOVH;
import org.kles.view.AbstractDataModelEditController;
import org.kles.view.ConnectionEditDialogController;
import org.kles.view.MainController;
import org.kles.view.RootLayoutController;
import resources.Resource;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Locale locale;
    private Properties configProp;
    private final StringProperty title = new SimpleStringProperty(Resource.TITLE);
    public static final String configFileName = "config";
    public static ResourceBundle resourceMessage;
    public static String SKIN = "skin";
    public static String LANGUAGE = "lang";
    public static Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
    private RootLayoutController rootController;
    private HashMap<String, ObservableList> dataMap;
    private final Account account = new Account();
    private final DigitalClock clock = new DigitalClock(DigitalClock.CLOCK);
    private final LinkedHashMap<String, String> listSkin = new LinkedHashMap<>();
    public static final Image LOGO_IMAGE = new Image(RootLayoutController.class.getResourceAsStream("/resources/images/logo.png"));
    private RestFulOVH ovhAPI;

**
     * Constructor
     */
    public MainApp() {
        clock.start();
        if (prefs.get(LANGUAGE, null) == null) {
            prefs.put(LANGUAGE, Locale.getDefault().toString());
        } else {
            Locale.setDefault(new Locale(prefs.get(LANGUAGE, null).split("_")[0], prefs.get(LANGUAGE, null).split("_")[1]));
        }
        locale = Locale.getDefault();
        resourceMessage = ResourceBundle.getBundle("resources/language", Locale.getDefault());
        loadSkins();
        if (prefs.get(SKIN, null) == null) {
            prefs.put(SKIN, STYLESHEET_MODENA);
        }
        Application.setUserAgentStylesheet(prefs.get(SKIN, null));
        if (prefs.get("connection.url", null) == null) {
            prefs.put("connection.url", "ssl0.ovh.net");
        }
        if (prefs.get("connection.port", null) == null) {
            prefs.put("connection.port", "993");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        title.bind(Bindings.concat(Resource.TITLE).concat("\t").concat(clock.getTimeText()));
        this.primaryStage = primaryStage;
        this.primaryStage.titleProperty().bind(title);
        this.primaryStage.getIcons().add(new Image("file:/resources/images/logo.png"));
        initRootLayout();
    }

    private void loadSkins() {
        listSkin.put("CASPIAN", STYLESHEET_CASPIAN);
        listSkin.put("MODENA", STYLESHEET_MODENA);
        listSkin.put("DarkTheme", "org/kles/css/DarkTheme.css");
        listSkin.put("Windows 7", "org/kles/css/Windows7.css");
        listSkin.put("JMetroDarkTheme", "org/kles/css/JMetroDarkTheme.css");
        listSkin.put("JMetroLightTheme", "org/kles/css/JMetroLightTheme.css");

    }

**
     * Initializes the root layout and tries to load the last opened person
     * file.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", locale));
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            UndecoratorScene scene = new UndecoratorScene(primaryStage, rootLayout);
            scene.getStylesheets().add(getClass().getResource("/org/kles/css/application.css").toExternalForm());
            scene.setFadeInTransition();
            primaryStage.setOnCloseRequest((WindowEvent we) -> {
                we.consume();
                scene.setFadeOutTransition();
            });
            primaryStage.setScene(scene);
            rootController = loader.getController();
            rootController.setMainApp(this);
            scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
                System.out.println("Width: " + newSceneWidth);
            });
            scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
                System.out.println("Height: " + newSceneHeight);
            });
            Undecorator undecorator = scene.getUndecorator();
            primaryStage.setMinWidth(undecorator.getMinWidth());
            primaryStage.setMinHeight(undecorator.getMinHeight());
            showMain();
            LoginManager loginManager = new LoginManager(this, primaryStage);
            loginManager.showLoginScreen();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMain() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", locale));
            loader.setLocation(MainApp.class.getResource("view/Main.fxml"));
            rootLayout.setCenter(loader.load());
            MainController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showConnectionEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", Locale.getDefault()));
            loader.setLocation(MainApp.class.getResource("view/ConnectionEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(resourceMessage.getString("connection.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(Resource.LOGO_ICON);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/org/kles/css/application.css").toExternalForm());
            dialogStage.setScene(scene);
            ConnectionEditDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

**
     * Opens a dialog to edit details for the specified datamodel. If the user
     * clicks OK, the changes are saved into the provided datamodel object and
     * true is returned.
     *
     * @param model the datamodel object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showDataModelEditDialog(AbstractDataModel model) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", Locale.getDefault()));
            loader.setLocation(MainApp.class.getResource("view/" + model.datamodelName() + "EditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(resourceMessage.getString(model.datamodelName().toLowerCase() + ".title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(Resource.LOGO_ICON);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            dialogStage.setScene(scene);
            AbstractDataModelEditController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setDataModel(model);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Account getAccountData() {
        return account;
    }

    public ResourceBundle getResourceMessage() {
        return resourceMessage;
    }

    public void setResourceMessage(ResourceBundle resourceMessage) {
        MainApp.resourceMessage = resourceMessage;
    }

    public HashMap<String, ObservableList> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, ObservableList> dataMap) {
        this.dataMap = dataMap;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public RootLayoutController getRootController() {
        return rootController;
    }

    public void setRootController(RootLayoutController rootController) {
        this.rootController = rootController;
    }

    public LinkedHashMap<String, String> getListSkin() {
        return listSkin;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initRestFulOVH(String account) {
        ovhAPI = new RestFulOVH(account);
    }

    public RestFulOVH getRestFulOVH() {
        return ovhAPI;
    }
}
