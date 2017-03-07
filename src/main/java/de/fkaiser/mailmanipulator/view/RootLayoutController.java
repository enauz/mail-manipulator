package de.fkaiser.mailmanipulator.view;

import de.fkaiser.mailmanipulator.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    @FXML
    private ProgressIndicator statusIndicator;

    @FXML
    private Label statusLabel;

    public MainApp getMainApp() {
        return this.mainApp;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public ProgressIndicator getStatusIndicator() {
        return this.statusIndicator;
    }

    public Label getStatusLabel() {
        return this.statusLabel;
    }

    public synchronized void setStatus(String statusText) {

        this.statusIndicator.setVisible(true);
        this.statusLabel.setVisible(true);
        this.statusLabel.setText(statusText);
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("MailManipulator");
        alert.setHeaderText("About");
        alert.setContentText("Author: Florian Kaiser\nWebsite: http://bioforscher.de");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        this.mainApp.getMailData().clear();
        this.mainApp.setMailFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(this.mainApp.getPrimaryStage());

        if (file != null) {
            this.mainApp.loadMailDataFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File personFile = this.mainApp.getMailFilePath();
        if (personFile != null) {
            this.mainApp.saveMailDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(this.mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            this.mainApp.saveMailDataToFile(file);
        }
    }

    @FXML
    private void handleSettings() {

        this.mainApp.showSettingsDialog();
    }
}