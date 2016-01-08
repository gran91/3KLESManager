package org.kles.view;

import org.kles.fx.custom.FxUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.kles.MainApp;
import org.kles.model.AbstractDataModel;

public class ModelManagerTableViewController extends AbstractModelManagerController implements IModelManagerView {

    @FXML
    protected TableView<AbstractDataModel> table;

    public ModelManagerTableViewController(String dataname) {
        super(dataname);
    }

**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        loadColumnTable();
    }

    public void loadColumnTable() {

    }

**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        super.setMainApp(mainApp);
        listData = mainApp.getDataMap().get(datamodelname);
        table.setItems(listData);
    }

    @Override
    public <T> void setData(ObservableList<T> listData) {
        this.listData = listData;
        table.setItems(this.listData);
    }

**
     * Called when the user clicks on the delete button.
     */
    @FXML
    @Override
    public void handleDelete() {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().remove(selectedIndex);
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, resourseMessage.getString("main.delete"), resourseMessage.getString("main.noselection"), String.format(resourseMessage.getString("message.noselection"), resourseMessage.getString(datamodelname.toLowerCase() + ".label").toLowerCase()));
        }
    }

**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new server.
     */
    @FXML
    @Override
    public void handleCopy() {
        datamodel = table.getSelectionModel().getSelectedItem();
        super.handleCopy();
    }

**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected datamodel.
     */
    @FXML
    @Override
    public void handleEdit() {
        datamodel = table.getSelectionModel().getSelectedItem();
        super.handleEdit();
    }
}
