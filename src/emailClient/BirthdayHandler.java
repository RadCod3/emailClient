package emailClient;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BirthdayHandler implements Runnable {

    private HashMap<MonthDay, List<String>> emailAddressesByBirthday = new HashMap<MonthDay, List<String>>();

    private IMediator mediator;

    public BirthdayHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    public void checkForWishableToday() {
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
        MonthDay dateMDay = MonthDay.from(date);
        List<MailRecipient> bdayRecipientList = new ArrayList<MailRecipient>();

        if (emailAddressesByBirthday.containsKey(dateMDay)) {
            List<String> bdayList = emailAddressesByBirthday.get(dateMDay);
            for (String emailString : bdayList) {
                bdayRecipientList.add(mediator.getRecipientByEmail(emailString));
            }
        } else {
            return null;
        }

        return bdayRecipientList;
    }

    public void addToBdayTable(LocalDate birthday, String emailString) {
        MonthDay bdayMonthDay = MonthDay.from(birthday);

        if (emailAddressesByBirthday.containsKey(bdayMonthDay)) {
            emailAddressesByBirthday.get(bdayMonthDay).add(emailString);
        } else {
            List<String> bdayList = new ArrayList<String>();
            bdayList.add(emailString);
            emailAddressesByBirthday.put(bdayMonthDay, bdayList);
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
