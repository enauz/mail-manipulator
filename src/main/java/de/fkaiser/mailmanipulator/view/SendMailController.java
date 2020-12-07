package de.fkaiser.mailmanipulator.view;

import de.fkaiser.mailmanipulator.MailManipulatorConstants;
import de.fkaiser.mailmanipulator.MainApp;
import de.fkaiser.mailmanipulator.model.Mail;
import de.fkaiser.mailmanipulator.model.MyMimeMessage;
import javafx.concurrent.Task;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class SendMailController extends Task<Void> {

    private final RootLayoutController rootLayoutController;
    private final Mail mail;

    public SendMailController(RootLayoutController rootLayoutController, Mail mail) {

        this.mail = mail;

        this.mail.setStatus("last sent at " + LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        this.rootLayoutController = rootLayoutController;

        this.rootLayoutController.getStatusIndicator().visibleProperty().bind(this.runningProperty());
        this.rootLayoutController.getStatusLabel().visibleProperty().bind(this.runningProperty());
        this.rootLayoutController.getStatusLabel().textProperty().bind(this.messageProperty());
    }

    @Override
    protected Void call() {

        // TODO add several manipulated received headers

        try {

            for (String recipient : this.mail.getTo().split(",")) {

                recipient = recipient.replace(" ", "");

                updateMessage("Preparing to send mail to " + recipient + "...");

                Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

                Properties mailSettings = new Properties();

                String socksHost = prefs.get("socksHost", null);
                String socksPort = prefs.get("socksPort", null);

                if (socksHost != null && socksHost.length() != 0 && socksPort != null && socksPort.length() != 0) {

                    mailSettings.put("mail.smtp.socks.host", socksHost);
                    mailSettings.put("mail.smtp.socks.port", socksPort);

                }

                mailSettings.put("mail.smtp.host", prefs.get("smtpHost", null));
                mailSettings.put("mail.smtp.port", prefs.get("smtpPort", null));
                mailSettings.put("mail.smtp.auth", MailManipulatorConstants.SMTP_AUTH);
                mailSettings.put("mail.smtp.starttls.enable", MailManipulatorConstants.SMTP_STARTTLS);
                mailSettings.put("mail.smtp.localhost", "localhost");

                Authenticator auth = new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(prefs.get("smtpUser", null), prefs.get("smtpPass", null));
                    }
                };

                updateMessage("Trying to login on SMTP server...");

                Session session = Session.getDefaultInstance(mailSettings, auth);

                updateMessage("Successfully logged in!");

                MyMimeMessage msg = new MyMimeMessage(session);

                msg.setFrom(new InternetAddress(this.mail.getFromEmail(), this.mail.getFromName()));

                msg.setSender(new InternetAddress(this.mail.getFromEmail(), this.mail.getFromName()));

                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient, recipient));
                msg.setSubject(this.mail.getSubject());

                msg.setSentDate(Date.from(this.mail.getDate().atZone(ZoneId.systemDefault()).toInstant()));

                msg.setMessageId(this.mail.getId());

                msg.setText(this.mail.getMessage(), "utf-8", this.mail.getType());

                msg.saveChanges();

                updateMessage("Sending mail to " + recipient + "...");

                Transport.send(msg);

                updateMessage("Mail successfully sent!");

            }
        } catch (Exception e) {

            updateMessage("Error while sending mail: " + e.getMessage());

        } finally {

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }
}
