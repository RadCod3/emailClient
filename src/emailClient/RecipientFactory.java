package emailClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.mail.internet.InternetAddress;

/**
 * It's a factory that creates recipients and stores them in a hashmap
 */
public class RecipientFactory {
    // HashMaps that store recipients by email and stores emails by birthdays
    private HashMap<String, MailRecipient> recipientsByEmail = new HashMap<String, MailRecipient>();

    private IMediator mediator;

    public RecipientFactory(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * It creates a new mail recipient based on the type of recipient and the
     * arguments passed to it
     * 
     * @param type      String
     * @param arguments [name, email, designation, etc.]
     * @return A boolean value.
     */
    public boolean createMailRecipient(String type, String[] arguments) {

        String name = arguments[0];
        String emailString;
        // TODO Date format decider. Could try to match multiple formats later
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String designation = null;
        String nickName = null;
        MailRecipient recipient = null;
        LocalDate birthday = null;

        if (type.equals("official")) {
            emailString = arguments[1];
            designation = arguments[2];

        } else if (type.equals("office_friend")) {
            emailString = arguments[1];
            designation = arguments[2];
            birthday = InputHandler.dateValidityCheck(arguments[3], formatter);
            if (birthday == null) {
                return false;
            }

        } else if (type.equals("personal")) {
            nickName = arguments[1];
            emailString = arguments[2];
            birthday = InputHandler.dateValidityCheck(arguments[3], formatter);
            if (birthday == null) {
                return false;
            }
        } else {
            return false;
        }

        InternetAddress emailAddress = mediator.emailValidityCheck(emailString);

        if (emailAddress == null) {
            return false;
        }

        if (type.equals("official")) {
            recipient = new OfficialRecipient(name, emailAddress, designation);

        } else if (type.equals("office_friend")) {
            recipient = new OfficialFriendRecipient(name, emailAddress, designation,
                    birthday);

        } else if (type.equals("personal")) {
            recipient = new PersonalRecipient(name, nickName, emailAddress, birthday);

        } else {
            return false;
        }

        if (recipient instanceof IHasBirthday) {
            mediator.addToBdayTable(birthday, emailString);
        }

        recipientsByEmail.put(emailString, recipient);
        return true;
    }

    /**
     * It returns an array of integers, each of which is the number of recipients of
     * a certain type
     * 
     * @return The number of recipients of each type.
     */
    public static int[] getRecipientCount() {
        int[] counts = { MailRecipient.getCount(), OfficialRecipient.getCount(), OfficialFriendRecipient.getCount(),
                PersonalRecipient.getCount() };
        return counts;
    }

    /**
     * If the emailString is a key in the recipientsByEmail HashMap, return the
     * value associated with that
     * key. Otherwise, return null
     * 
     * @param emailString the email address of the recipient
     * @return The value of the key emailString in the recipientsByEmail HashMap.
     */
    public MailRecipient getRecipientByEmail(String emailString) {
        return hasRecipient(emailString) ? recipientsByEmail.get(emailString) : null;
    }

    /**
     * This function checks if the recipient exists in the recipientsByEmail HashMap
     * 
     * @param emailString The email address of the recipient to check for.
     * @return A boolean value.
     */
    public boolean hasRecipient(String emailString) {
        return recipientsByEmail.containsKey(emailString);
    }
}
