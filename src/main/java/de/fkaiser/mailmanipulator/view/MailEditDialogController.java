package de.fkaiser.mailmanipulator.view;

import de.fkaiser.mailmanipulator.model.Mail;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class MailEditDialogController {

    @FXML
    private TextField toField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField fromEmailField;
    @FXML
    private TextField fromNameField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField idField;
    @FXML
    private TextField statusField;
    @FXML
    private TextArea messageEditor;

    private Stage dialogStage;
    private Mail mail;
    private boolean okClicked = false;

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

    public void setMail(Mail mail) {
        this.mail = mail;

        this.toField.setText(mail.getTo());
        this.toField.setPromptText("mail@domain.com");
        this.subjectField.setText(mail.getSubject());
        this.dateField.setText(mail.getDate().toString());
        this.idField.setText(mail.getId());
        this.fromEmailField.setText(mail.getFromEmail());
        this.fromNameField.setText(mail.getFromName());
        this.statusField.setText(mail.getStatus());
        this.messageEditor.setText(mail.getMessage());
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        this.dialogStage.close();
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {

            this.mail.setTo(this.toField.getText());
            this.mail.setSubject(this.subjectField.getText());
            this.mail.setDate(LocalDateTime.parse(this.dateField.getText()));
            this.mail.setStatus(this.statusField.getText());
            this.mail.setMessage(this.messageEditor.getText());
            this.mail.setFromEmail(this.fromEmailField.getText());
            this.mail.setFromName(this.fromNameField.getText());
            this.mail.setId(this.idField.getText());

            this.okClicked = true;
            this.dialogStage.close();

        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (this.toField.getText() == null || this.toField.getText().length() == 0) {
            errorMessage += "No valid recipient!\n";
        }
        if (this.subjectField.getText() == null || this.subjectField.getText().length() == 0) {
            errorMessage += "No valid subject!\n";
        }
        if (this.dateField.getText() == null || this.dateField.getText().length() == 0) {
            errorMessage += "No valid date!\n";
        } else {
            try {
                LocalDateTime.parse(this.dateField.getText());
            } catch (DateTimeParseException e) {
                errorMessage += "No valid date entered!\n";
            }
        }

        if (this.idField.getText() == null || this.idField.getText().length() == 0) {
            errorMessage += "No valid message ID!\n";
        }

        if (this.fromEmailField.getText() == null || this.fromEmailField.getText().length() == 0) {
            errorMessage += "No valid sender email!\n";
        }

        if (this.fromNameField.getText() == null || this.fromNameField.getText().length() == 0) {
            errorMessage += "No valid sender name!\n";
        }

        if (this.statusField.getText() == null || this.statusField.getText().length() == 0) {
            errorMessage += "No valid status!\n";
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
