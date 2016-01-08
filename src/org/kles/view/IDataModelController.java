/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kles.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.kles.model.AbstractDataModel;

/**
 *
 * @author JCHAUT
 */
public interface IDataModelController {

    public void setDataModel(AbstractDataModel datamodel);

    public boolean isOkClicked();

    public boolean isInputValid();

    @FXML
    public void handleCancel();

    @FXML
    public void handleOk();

    public void setDialogStage(Stage dialogStage);
}
