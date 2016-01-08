package org.kles.view;

import javafx.fxml.FXML;
import org.kles.MainApp;

/**
 * FXML Controller class
 *
 * @author Jeremy.CHAUT
 */
public class MainController {

    @FXML
    private AccountInformationController accountInformationController;
    @FXML
    private ChangePasswordController changePasswordController;
    @FXML
    private ResponderManagementController responderManagementController;

    private MainApp mainApp;

    @FXML
    private void initialize() {
    }

**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        accountInformationController.setMainApp(mainApp);
        changePasswordController.setMainApp(mainApp);
        responderManagementController.setMainApp(mainApp);
    }
}
