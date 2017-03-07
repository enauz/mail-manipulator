package de.fkaiser.mailmanipulator.model;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Created by fkaiser on 3/7/17.
 */
public class MyMimeMessage extends MimeMessage {

    private String messageId;

    public MyMimeMessage(Session session) {
        super(session);
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) throws MessagingException {

        this.messageId = messageId;
        updateMessageID();
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        setHeader("Message-Id", this.messageId);
        // removeHeader("Message-Id");

    }
}
