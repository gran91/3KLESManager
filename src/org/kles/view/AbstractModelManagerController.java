package org.kles.view;

import org.kles.fx.custom.FxUtil;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.kles.MainApp;
import org.kles.model.AbstractDataModel;

public abstract class AbstractModelManagerController implements IModelManagerView {

/ Reference to the main application.
    protected MainApp mainApp;
    protected ResourceBundle resourseMessage;
    protected String datamodelname = "";
    protected AbstractDataModel datamodel;
    protected ObservableList listData;

**
     * The constructor. The constructor is called before the initialize()
     * method.
     *
     * @param dataname
     */
    public AbstractModelManagerController(String dataname) {
        this.listData = FXCollections.observableArrayList();
        datamodelname = dataname;
    }

**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
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
        resourseMessage = mainApp.getResourceMessage();
    }

    public <T> void setData(ObservableList<T> listData) {

    }

**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new datamodel.
     */
    @FXML
    @Override
    public void handleNew() {
        datamodel = datamodel.newInstance();
        boolean okClicked = mainApp.showDataModelEditDialog(datamodel);
        if (okClicked) {
            listData.add(datamodel);
        }
    }

**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new datamodel.
     */
    @FXML
    @Override
    public void handleCopy() {
        if (datamodel != null) {
            AbstractDataModel tempData = datamodel.newInstance();
            tempData.populateData(datamodel.extractData());
            boolean okClicked = mainApp.showDataModelEditDialog(tempData);
            if (okClicked) {
                listData.add(tempData);
            }
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, resourseMessage.getString("main.delete"), resourseMessage.getString("main.noselection"), String.format(datamodelname + ".noselection" + resourseMessage.getString(datamodelname.toLowerCase() + ".label")));
        }
    }

**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected datamodel.
     */
    @FXML
    @Override
    public void handleEdit() {
        if (datamodel != null) {
            boolean okClicked = mainApp.showDataModelEditDialog(datamodel);
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, resourseMessage.getString("main.delete"), resourseMessage.getString("main.noselection"), String.format(datamodelname + ".noselection" + resourseMessage.getString(datamodelname.toLowerCase() + ".label")));
        }
    }

    @FXML
    protected void onKeyPressed(KeyEvent event) {
        if (event.isControlDown() && event.getCode().equals(KeyCode.DIGIT1)) {
            handleNew();
        }
        if (event.isControlDown() && event.getCode().equals(KeyCode.DIGIT2)) {
            handleEdit();
        }
        if (event.isControlDown() && event.getCode().equals(KeyCode.DIGIT3)) {
            handleCopy();
        }
        if (event.isControlDown() && event.getCode().equals(KeyCode.DIGIT4)) {
            handleDelete();
        }
    }

    @FXML
    protected void onMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            handleEdit();
        }
    }

    public String getDatamodelname() {
        return datamodelname;
    }
}
