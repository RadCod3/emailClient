package emailClient;

import javax.mail.internet.InternetAddress;

/**
 * OfficialRecipient is a subclass of MailRecipient that adds a designation
 * field
 */
public class OfficialRecipient extends MailRecipient {
    private String designation;
    private static int count = 0;

    OfficialRecipient(String name, InternetAddress emailAddress, String designation) {
        super(name, emailAddress);
        this.designation = designation;
        count++;
    }

    public String getDesignation() {
        return designation;
    }

    public static int getCount() {
        return count;
    }

}
