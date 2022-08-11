package emailClient;

import javax.mail.internet.InternetAddress;

/**
 * The EmailFactory class is a factory class that creates Email objects
 */
public class EmailFactory {

    private String signature = "";

    /**
     * This function creates an email object and sets the recipient, subject and
     * content
     * 
     * @param mailRecipient The recipient of the email.
     * @param subject       The subject of the email
     * @param content       The content of the email.
     * @return An email object.
     */
    public Email createEmail(MailRecipient mailRecipient, String subject, String content) {
        Email email = new Email();
        email.setRecipient(mailRecipient);
        email.setSubject(subject);
        email.setContent(content + signature);
        return email;
    }

    /**
     * This function creates an email object and sets the recipientEmailAddress,
     * subject and
     * content
     * 
     * @param emailAddress The email address of the recipient.
     * @param subject      The subject of the email
     * @param content      The content of the email.
     * @return An email object.
     */
    public Email createEmail(InternetAddress emailAddress, String subject, String content) {
        Email email = new Email();
        email.setRecipientEmailAddress(emailAddress);
        email.setSubject(subject);
        email.setContent(content);
        return email;
    }

    /**
     * If the mailRecipient is an instance of IHasBirthday, then create an email
     * with the content and
     * subject of the mailRecipient's birthday wishes.
     * 
     * @param mailRecipient The recipient of the email.
     * @return Email
     */
    public Email createBirthdayEmail(MailRecipient mailRecipient) {
        if (mailRecipient instanceof IHasBirthday) {
            IHasBirthday birthdayRecipient = (IHasBirthday) mailRecipient;
            Email email = new Email();
            email.setRecipient(mailRecipient);
            email.setContent("Dear " + mailRecipient.getName().split(" ")[0] + ",\n" +
                    birthdayRecipient.wishForBirthdayContent() + "\n\n" +
                    signature);
            email.setSubject(birthdayRecipient.wishForBdaySubject());
            return email;
        } else {
            return null;
        }
    }

    public void setSignature(String signature) {
        this.signature = signature.replaceAll("\\\\n", "\n");
    }

    public String getSignature() {
        return signature;
    }

}
