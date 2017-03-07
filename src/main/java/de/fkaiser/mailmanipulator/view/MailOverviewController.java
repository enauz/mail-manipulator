package de.fkaiser.mailmanipulator.view;

import de.fkaiser.mailmanipulator.MainApp;
import de.fkaiser.mailmanipulator.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class MailOverviewController {

    @FXML
    private TableView<Mail> mailTable;

    @FXML
    private TableColumn<Mail, String> toColumn;

    @FXML
    private TableColumn<Mail, String> subjectColumn;

    @FXML
    private TableColumn<Mail, String> statusColumn;

    @FXML
    private Label toLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label fromEmailLabel;

    @FXML
    private Label fromNameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button sendButton;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public MailOverviewController() {

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        this.mailTable.setItems(this.mainApp.getMailData());

    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteMail() {
        int selectedIndex = this.mailTable.getSelectionModel()
                                          .getSelectedIndex();
        if (selectedIndex >= 0) {
            this.mailTable.getItems().remove(selectedIndex);
        } else {
            showNoSelectionDialog();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditMail() {
        Mail selectedMail = this.mailTable.getSelectionModel()
                                          .getSelectedItem();
        if (selectedMail != null) {
            boolean okClicked = this.mainApp.showMailEditDialog(selectedMail);
            if (okClicked) {
                showMailDetails(selectedMail);
            }

        } else {

            showNoSelectionDialog();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewMail() {
        Mail tempMail = new Mail();
        boolean okClicked = this.mainApp.showMailEditDialog(tempMail);
        if (okClicked) {
            this.mainApp.getMailData().add(tempMail);
        }
    }

    @FXML
    private void handleSend() {

        Mail selectedMail = this.mailTable.getSelectionModel()
                                          .getSelectedItem();
        if (selectedMail != null) {

            this.mainApp.sendMail(selectedMail);

            showMailDetails(selectedMail);
        } else {

            showNoSelectionDialog();
        }

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        this.toColumn.setCellValueFactory(cellData -> cellData.getValue()
                                                              .toProperty());

        this.subjectColumn.setCellValueFactory(cellData -> cellData.getValue()
                                                                   .subjectProperty());

        this.statusColumn.setCellValueFactory(cellData -> cellData.getValue()
                                                                  .statusProperty());

        // clear mail details
        showMailDetails(null);

        // Listen for selection changes and show the person details when
        // changed.
        this.mailTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> showMailDetails(newValue));

        // enable edit, delete and send buttons only if item is selected
        this.editButton.disableProperty().bind(
                this.mailTable.getSelectionModel().selectedItemProperty()
                              .isNull());
        this.sendButton.disableProperty().bind(
                this.mailTable.getSelectionModel().selectedItemProperty()
                              .isNull());
        this.deleteButton.disableProperty().bind(
                this.mailTable.getSelectionModel().selectedItemProperty()
                              .isNull());
    }

    private void showMailDetails(Mail mail) {

        if (mail != null) {
            // Fill the labels with info from the mail object.
            this.toLabel.setText(mail.getTo());
            this.subjectLabel.setText(mail.getSubject());
            this.fromEmailLabel.setText(mail.getFromEmail());
            this.fromNameLabel.setText(mail.getFromName());
            this.dateLabel.setText(mail.getDate().toString());
            this.idLabel.setText(mail.getId());
            this.statusLabel.setText(mail.getStatus());

        } else {
            // mail is null, remove all the text.
            this.toLabel.setText("");
            this.subjectLabel.setText("");
            this.fromEmailLabel.setText("");
            this.fromNameLabel.setText("");
            this.dateLabel.setText("");
            this.idLabel.setText("");
            this.statusLabel.setText("");
        }
    }

    private void showNoSelectionDialog() {
        // Nothing selected.
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(this.mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Mail Selected");
        alert.setContentText("Please select a mail in the table.");

        alert.showAndWait();
    }
}
