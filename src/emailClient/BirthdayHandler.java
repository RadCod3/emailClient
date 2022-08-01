package emailClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirthdayHandler implements Runnable {

    private HashMap<LocalDate, List<String>> emailAddressesByBirthday = new HashMap<LocalDate, List<String>>();

    private IMediator mediator;

    public BirthdayHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    public void checkForWishableToday() {
        // FIXME birthday checks the exact date instead of month
        LocalDate today = LocalDate.now();

        List<MailRecipient> bdayRecipientList = getWishable(today);

        if (bdayRecipientList != null) {
            for (MailRecipient mailRecipient : bdayRecipientList) {
                if (alreadyWished(mailRecipient)) {
                    continue;
                }
                Email email = mediator.createBirthdayEmail(mailRecipient);
                mediator.sendEmail(email, true);
            }
        }

    }

    public List<MailRecipient> getWishable(LocalDate date) {

        List<MailRecipient> bdayRecipientList = new ArrayList<MailRecipient>();

        if (emailAddressesByBirthday.containsKey(date)) {
            List<String> bdayList = emailAddressesByBirthday.get(date);
            for (String emailString : bdayList) {
                bdayRecipientList.add(mediator.getRecipientByEmail(emailString));
            }
        } else {
            return null;
        }

        return bdayRecipientList;
    }

    public void addToBdayTable(LocalDate birthday, String emailString) {
        if (emailAddressesByBirthday.containsKey(birthday)) {
            emailAddressesByBirthday.get(birthday).add(emailString);
        } else {
            List<String> bdayList = new ArrayList<String>();
            bdayList.add(emailString);
            emailAddressesByBirthday.put(birthday, bdayList);
        }

    }

    public boolean alreadyWished(MailRecipient mailRecipient) {
        IHasBirthday hasBirthdayRecipient = (IHasBirthday) mailRecipient;
        List<Email> sentEmails = mediator.getSentEmailsOnDate(LocalDate.now());
        if (sentEmails != null) {
            for (Email email : sentEmails) {
                boolean birthdayRecipientGotAMail = email.getRecipientEmailAddress() == mailRecipient.getEmail();
                boolean isBirthdayEmail = email.getSubject().equals(hasBirthdayRecipient.wishForBdaySubject());
                if (birthdayRecipientGotAMail && isBirthdayEmail) {
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void run() {
        checkForWishableToday();

    }
}
