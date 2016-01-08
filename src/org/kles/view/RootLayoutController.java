package org.kles.view;

import org.kles.fx.custom.FxUtil;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;

import org.kles.MainApp;
import resources.Resource;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Jérémy Chaut
 */
public class RootLayoutController {

    @FXML
    private RadioMenuItem menuFR;
    @FXML
    private RadioMenuItem menuEN;
    @FXML
    private Menu langmenu, skinmenu;
    private MainApp mainApp;

**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainApp.getListSkin().entrySet().stream().forEach((nameSkin) -> {
            final CheckMenuItem menuItemSkin = new CheckMenuItem(nameSkin.getKey());
            menuItemSkin.setSelected(this.mainApp.prefs.get(MainApp.SKIN, null).equals(nameSkin.getKey()));
            menuItemSkin.setOnAction(e -> {
                Application.setUserAgentStylesheet(nameSkin.getValue());
                this.mainApp.prefs.put(MainApp.SKIN, nameSkin.getKey());
                skinmenu.getItems().stream().forEach(m -> {
                    ((CheckMenuItem) m).setSelected(m.getText().equals(nameSkin.getKey()));
                });
            });
            skinmenu.getItems().add(menuItemSkin);
        });
        langmenu.getItems().stream().forEach(m -> {
            ((RadioMenuItem) m).setSelected(this.mainApp.prefs.get(MainApp.LANGUAGE, null).equals(m.getId()));
            m.setOnAction(e -> {
                Locale.setDefault(new Locale(m.getId()));
                this.mainApp.prefs.put(MainApp.LANGUAGE, m.getId());
                langmenu.getItems().stream().forEach(m1 -> {
                    ((RadioMenuItem) m).setSelected(m1.getId().equals(this.mainApp.prefs.get(MainApp.LANGUAGE, null)));
                });
                try {
                    mainApp.getPrimaryStage().close();
                } catch (Exception ex) {
                    Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainApp().start(new Stage());
            });
        });
    }

**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        FxUtil.showAlert(Alert.AlertType.INFORMATION, mainApp.getResourceMessage().getString("about.title"), String.format(mainApp.getResourceMessage().getString("about.header"), Resource.VERSION), String.format(mainApp.getResourceMessage().getString("about.text"), Resource.VERSION));
    }

**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void showConnection() {
        mainApp.showConnectionEditDialog();
    }
}
