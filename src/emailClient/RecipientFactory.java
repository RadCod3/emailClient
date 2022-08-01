package emailClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.mail.internet.InternetAddress;

public class RecipientFactory {
    // HashMaps that store recipients by email and stores emails by birthdays
    private HashMap<String, MailRecipient> recipientsByEmail = new HashMap<String, MailRecipient>();

    private IMediator mediator;

    public RecipientFactory(IMediator mediator) {
        this.mediator = mediator;
    }

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

    public static int[] getRecipientCount() {
        int[] counts = { MailRecipient.getCount(), OfficialRecipient.getCount(), OfficialFriendRecipient.getCount(),
                PersonalRecipient.getCount() };
        return counts;
    }

    public MailRecipient getRecipientByEmail(String emailString) {
        return hasRecipient(emailString) ? recipientsByEmail.get(emailString) : null;
    }

    public boolean hasRecipient(String emailString) {
        return recipientsByEmail.containsKey(emailString);
    }
}
