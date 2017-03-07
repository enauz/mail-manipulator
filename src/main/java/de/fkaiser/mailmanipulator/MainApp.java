package de.fkaiser.mailmanipulator;

import de.fkaiser.mailmanipulator.model.Mail;
import de.fkaiser.mailmanipulator.model.MailListWrapper;
import de.fkaiser.mailmanipulator.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Created by fkaiser on 3/7/17.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private ObservableList<Mail> mailData = FXCollections.observableArrayList();
    private BorderPane rootLayout;
    private RootLayoutController rootLayoutController;

    public MainApp() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Mail> getMailData() {
        return this.mailData;
    }

    /**
     * Returns the mail file preference, i.e. the file that was last opened. The
     * preference is read from the OS specific registry. If no such preference
     * can be found, null is returned.
     *
     * @return
     */
    public File getMailFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setMailFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            this.primaryStage.setTitle("MailManipulator - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            this.primaryStage.setTitle("MailManipulator");
        }
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    /**
     * Loads mail data from the specified file. The current mail data will be
     * replaced.
     *
     * @param file
     */
    public void loadMailDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(MailListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            MailListWrapper wrapper = (MailListWrapper) um.unmarshal(file);

            this.mailData.clear();
            this.mailData.addAll(wrapper.getMails());

            // Save the file path to the registry.
            setMailFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n"
                                 + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current mail data to the specified file.
     *
     * @param file
     */
    public void saveMailDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(MailListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our mail data.
            MailListWrapper wrapper = new MailListWrapper();
            wrapper.setMails(this.mailData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setMailFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n"
                                 + file.getPath());

            alert.showAndWait();
        }
    }

    public void sendMail(Mail mail) {
        SendMailController smt = new SendMailController(
                this.rootLayoutController, mail);
        new Thread(smt).start();
    }

    /**
     * Opens a dialog to edit details for the specified mail. If the user clicks
     * OK, the changes are saved into the provided mail object and true is
     * returned.
     *
     * @param mail the mail object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showMailEditDialog(Mail mail) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("view/MailEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Mail");
            dialogStage.getIcons().add(
                    new Image("file:resources/images/icon.png"));

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the mail into the controller.
            MailEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMail(mail);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showSettingsDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("view/SettingsDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SMTP Settings");
            dialogStage.getIcons().add(
                    new Image("file:resources/images/icon.png"));

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setOnCloseRequest(Event::consume);
            // Set the mail into the controller.
            SettingsDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MailManipulator");
        this.primaryStage.getIcons().add(
                new Image("file:resources/images/icon.png"));

        initRootLayout();

        showMailOverView();

        if (!validSettings()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No SMTP settings found");
            alert.setContentText("Please configiure a SMTP server to use MailManipulator.");

            alert.showAndWait();

            showSettingsDialog();
        }
    }

    /**
     * checks if SMTP settings are valid
     *
     * @return true if valid SMTP settings
     */
    public boolean validSettings() {

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        if (prefs.get("smtpHost", null) == null
            || prefs.get("smtpHost", null).length() == 0) {

            return false;
        }
        if (prefs.get("smtpPort", null) == null
            || prefs.get("smtpPort", null).length() == 0) {

            return false;

        }
        if (prefs.get("smtpUser", null) == null
            || prefs.get("smtpUser", null).length() == 0) {

            return false;

        }
        return !(prefs.get("smtpPass", null) == null
                 || prefs.get("smtpPass", null).length() == 0);
    }

    private void initRootLayout() {

        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("view/RootLayout.fxml"));
            this.rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();

            this.rootLayoutController = loader.getController();
            this.rootLayoutController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows the mail overview scene
     */
    private void showMailOverView() {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Thread.currentThread().getContextClassLoader().getResource("view/MailOverview.fxml"));
            AnchorPane mailOverview = loader.load();

            this.rootLayout.setCenter(mailOverview);

            // Give the controller access to the main app.
            MailOverviewController controller = loader.getController();
            controller.setMainApp(this);

            // Try to load last opened mail file.
            File file = getMailFilePath();
            if (file != null) {
                loadMailDataFromFile(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
