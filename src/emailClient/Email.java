package emailClient;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.mail.internet.InternetAddress;

public class Email implements Serializable {
    private transient MailRecipient recipient = null;
    private InternetAddress recipientEmailAddress;
    private LocalDateTime sentDate;
    private String subject;
    private String content;
    private boolean isSent = false;

    public Email() {
        sentDate = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public MailRecipient getRecipient() {
        return recipient;
    }

    public InternetAddress getRecipientEmailAddress() {
        return recipientEmailAddress;
    }

    public LocalDate getSentDate() {
        return sentDate.toLocalDate();
    }

    public LocalDateTime getSentTime() {
        return sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecipient(MailRecipient recipient) {
        this.recipient = recipient;
        this.recipientEmailAddress = recipient.getEmail();
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setRecipientEmailAddress(InternetAddress recipientEmailAddress) {
        this.recipientEmailAddress = recipientEmailAddress;
    }

    public void setSent() {
        this.isSent = true;
    }

    public boolean wasSent() {
        return isSent;
    }

}
