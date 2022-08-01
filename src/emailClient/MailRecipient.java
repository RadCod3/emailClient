package emailClient;

import javax.mail.internet.InternetAddress;

public abstract class MailRecipient {
    private static int count = 0;
    private String name;
    private InternetAddress emailAddress;
    // private Date birthday;

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
