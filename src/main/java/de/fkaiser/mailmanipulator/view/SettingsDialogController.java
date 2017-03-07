package de.fkaiser.mailmanipulator.view;

import de.fkaiser.mailmanipulator.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class SettingsDialogController {

    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private TextField smtpHostField;
    @FXML
    private TextField smtpPortField;
    @FXML
    private TextField smtpUserField;
    @FXML
    private TextField smtpPassField;
    @FXML
    private TextField socksHostField;
    @FXML
    private TextField socksPortField;
    @FXML
    private Button cancelButton;

    // Reference to the main application.
    private MainApp mainApp;

    @FXML
    public void initialize() {

        System.out.println(Preferences.userRoot().absolutePath());
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        if (!(prefs.get("smtpHost", null) == null || prefs
                                                             .get("smtpHost", null).length() == 0)) {

            this.smtpHostField.setText(prefs.get("smtpHost", null));
        }
        if (!(prefs.get("smtpPort", null) == null || prefs
                                                             .get("smtpPort", null).length() == 0)) {

            this.smtpPortField.setText(prefs.get("smtpPort", null));

        }
        if (!(prefs.get("smtpUser", null) == null || prefs
                                                             .get("smtpUser", null).length() == 0)) {

            this.smtpUserField.setText(prefs.get("smtpUser", null));

        }
        if (!(prefs.get("smtpPass", null) == null || prefs
                                                             .get("smtpPass", null).length() == 0)) {

            this.smtpPassField.setText(prefs.get("smtpPass", null));

        }

        if (!(prefs.get("socksHost", null) == null || prefs.get("socksHost",
                                                                null).length() == 0)) {

            this.socksHostField.setText(prefs.get("socksHost", null));

        }

        if (!(prefs.get("socksPort", null) == null || prefs.get("socksPort",
                                                                null).length() == 0)) {

            this.socksPortField.setText(prefs.get("socksPort", null));

        }
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return this.okClicked;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {

        // disable cancel button if no valid settings detected
        if (!this.mainApp.validSettings()) {

            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Invalid Settings");
            alert.setHeaderText("Please enter SMTP settings to use MailManipulator!");

            alert.showAndWait();

            System.exit(0);

        } else {

            this.dialogStage.close();
        }
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {

            Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
            prefs.put("smtpHost", this.smtpHostField.getText());
            prefs.put("smtpPort", this.smtpPortField.getText());
            prefs.put("smtpUser", this.smtpUserField.getText());
            prefs.put("smtpPass", this.smtpPassField.getText());
            prefs.put("socksHost", this.socksHostField.getText());
            prefs.put("socksPort", this.socksPortField.getText());

            this.okClicked = true;
            this.dialogStage.close();
        }
    }

    private boolean isInputValid() {

        String errorMessage = "";

        if (this.smtpHostField.getText() == null
            || this.smtpHostField.getText().length() == 0) {
            errorMessage += "No valid SMTP host!\n";
        }
        if (this.smtpPortField.getText() == null
            || this.smtpPortField.getText().length() == 0) {
            errorMessage += "No valid SMTP port!\n";
        } else {
            // try to parse the port an int.
            try {
                Integer.parseInt(this.smtpPortField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid SMTP port entered!\n";
            }
        }
        if (this.smtpUserField.getText() == null
            || this.smtpUserField.getText().length() == 0) {
            errorMessage += "No valid SMTP user name!\n";
        }

        if (this.smtpPassField.getText() == null
            || this.smtpPassField.getText().length() == 0) {
            errorMessage += "No valid SMTP password!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
