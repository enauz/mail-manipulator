package de.fkaiser.mailmanipulator.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by fkaiser on 3/7/17.
 */
@XmlRootElement(name = "mails")
public class MailListWrapper {

    private List<Mail> mails;

    @XmlElement(name = "mail")
    public List<Mail> getMails() {
        return this.mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }
}