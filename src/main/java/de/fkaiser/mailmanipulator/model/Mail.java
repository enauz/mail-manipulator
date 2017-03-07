package de.fkaiser.mailmanipulator.model;

import de.fkaiser.mailmanipulator.util.LocalDateTimeAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by fkaiser on 3/7/17.
 */
public class Mail {

    private final StringProperty to;
    private final StringProperty subject;
    private final ObjectProperty<LocalDateTime> date;
    private final StringProperty id;
    private final StringProperty status;
    private final StringProperty message;
    private SimpleStringProperty fromEmail;
    private SimpleStringProperty fromName;

    public Mail() {
        this(null, null, null, null);
    }

    public Mail(String to, String subject, String fromEmail, String fromName) {

        this.to = new SimpleStringProperty(to);
        this.subject = new SimpleStringProperty(subject);
        this.fromEmail = new SimpleStringProperty(fromEmail);
        this.fromName = new SimpleStringProperty(fromName);

        // default values
        this.date = new SimpleObjectProperty<>(
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        this.id = new SimpleStringProperty("954145809.4206921429103923702.JavaMail.omsadm@oms-sap-prod");
        this.status = new SimpleStringProperty("never sent");
        this.message = new SimpleStringProperty("<html>" + "<body>Enter your message.</body>" + "</html>");
    }

    public ObjectProperty<LocalDateTime> dateProperty() {
        return this.date;
    }

    public StringProperty fromEmailProperty() {
        return this.fromEmail;
    }

    public StringProperty fromNameProperty() {
        return this.fromName;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getDate() {
        return this.date.get();
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public String getFromEmail() {
        return this.fromEmail.get();
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail.set(fromEmail);
    }

    public String getFromName() {
        return this.fromName.get();
    }

    public void setFromName(String fromName) {
        this.fromName.set(fromName);
    }

    public String getId() {
        return this.id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getMessage() {
        return this.message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getStatus() {
        return this.status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getSubject() {
        return this.subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getTo() {
        return this.to.get();
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public StringProperty idProperty() {
        return this.id;
    }

    public StringProperty messageProperty() {
        return this.message;
    }

    public StringProperty statusProperty() {

        return this.status;
    }

    public StringProperty subjectProperty() {
        return this.subject;
    }

    public StringProperty toProperty() {
        return this.to;
    }
}
