package emailClient;

import javax.mail.internet.InternetAddress;

/**
 * The MailRecipient class is an abstract class that represents a person who can
 * receive email
 */
public abstract class MailRecipient {
    private static int count = 0;
    private String name;
    private InternetAddress emailAddress;

    public MailRecipient(String name, InternetAddress emailAddress) {
        this.name = name;

        this.emailAddress = emailAddress;

        count++;
    }

    public String getName() {
        return name;
    }

    public static int getCount() {
        return count;
    }

    public InternetAddress getEmail() {
        return emailAddress;
    }
}
