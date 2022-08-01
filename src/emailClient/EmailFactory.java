package emailClient;

import javax.mail.internet.InternetAddress;

public class EmailFactory {

    public Email createEmail(MailRecipient mailRecipient, String subject, String content) {
        Email email = new Email();
        email.setRecipient(mailRecipient);
        email.setSubject(subject);
        email.setContent(content);
        return email;
    }

    public Email createEmail(InternetAddress emailAddress, String subject, String content) {
        Email email = new Email();
        email.setRecipientEmailAddress(emailAddress);
        email.setSubject(subject);
        email.setContent(content);
        return email;
    }

    public Email createBirthdayEmail(MailRecipient mailRecipient) {
        if (mailRecipient instanceof IHasBirthday) {
            IHasBirthday birthdayRecipient = (IHasBirthday) mailRecipient;
            Email email = new Email();
            email.setRecipient(mailRecipient);
            email.setContent(birthdayRecipient.wishForBirthdayContent());
            email.setSubject(birthdayRecipient.wishForBdaySubject());
            return email;
        } else {
            return null;
        }
    }
}
