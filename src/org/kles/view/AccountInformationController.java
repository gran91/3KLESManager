package org.kles.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.kles.MainApp;

/**
 * FXML Controller class
 *
 * @author Jeremy.CHAUT
 */
public class AccountInformationController {

    @FXML
    private Label labelAccount, labelDescription, labelUsage, msgcountLabel, inmsgcountLabel;
    @FXML
    ProgressBar progressUsage;

    private MainApp mainApp;
    private final DoubleProperty percent = new SimpleDoubleProperty();
    private final StringProperty usageValue = new SimpleStringProperty();

    @FXML
    private void initialize() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        labelAccount.textProperty().bind(mainApp.getAccountData().getEmailProperty().concat("@").concat(resources.Resource.DOMAIN));
        labelDescription.textProperty().bind(mainApp.getAccountData().getNameProperty());
        msgcountLabel.textProperty().bind(mainApp.getAccountData().getCountEmailProperty().asString().concat(" emails"));
        inmsgcountLabel.textProperty().bind(mainApp.getAccountData().getInEmailProperty().asString().concat(" emails"));

        progressUsage.progressProperty().bind(percent);
        labelUsage.textProperty().bind(usageValue);
        mainApp.getAccountData().getQuotaProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                percent.set((double) mainApp.getAccountData().getQuota() / mainApp.getAccountData().getSize());
                usageValue.set(convertSizeToString(mainApp.getAccountData().getQuota()) + " / " + convertSizeToString(mainApp.getAccountData().getSize()));
            }
        });

        mainApp.getAccountData().getSizeProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                percent.set((double) mainApp.getAccountData().getQuota() / mainApp.getAccountData().getSize());
                usageValue.set(convertSizeToString(mainApp.getAccountData().getQuota()) + " / " + convertSizeToString(mainApp.getAccountData().getSize()));
            }
        });
        progressUsage.progressProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null) {
                    if (newValue.doubleValue() < 0.5) {
                        progressUsage.setStyle("-fx-accent: #00CC00;");

                    } else if (newValue.doubleValue() > 0.5 && newValue.doubleValue() < 0.75) {
                        progressUsage.setStyle("-fx-accent: #FFC125;");
                    } else if (newValue.doubleValue() > 0.75) {
                        progressUsage.setStyle("-fx-accent: #DD0000;");
                    }
                }
            }
        });
    }

    private String convertSizeToString(long octet) {
        if (octet < Math.pow(10, 3)) {
            return octet + " o";
        } else if (octet < Math.pow(10, 6)) {
            return String.format("%.2f", (double) octet / Math.pow(10, 3)) + " Ko";
        } else if (octet < Math.pow(10, 9)) {
            return String.format("%.2f", (double) octet / Math.pow(10, 6)) + " Mo";
        } else if (octet < Math.pow(10, 12)) {
            return String.format("%.2f", (double) octet / Math.pow(10, 9)) + " Go";
        } else if (octet < Math.pow(10, 15)) {
            return String.format("%.2f", (double) octet / Math.pow(10, 12)) + " To";
        }
        return octet + " o";
    }
}
