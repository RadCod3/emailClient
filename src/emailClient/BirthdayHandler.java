package emailClient;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The BirthdayHandler class as the name suggests "Handles" things related to
 * birthdays.
 */

public class BirthdayHandler implements Runnable {

    private HashMap<MonthDay, List<String>> emailAddressesByBirthday = new HashMap<MonthDay, List<String>>();

    private IMediator mediator;

    public BirthdayHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * If there are any birthdays today, wish them.
     */
    public void checkForWishableToday() {
        LocalDate today = LocalDate.now();

        List<MailRecipient> bdayRecipientList = getWishable(today);

        if (bdayRecipientList != null) {
            for (MailRecipient mailRecipient : bdayRecipientList) {
                wishForBirthday(mailRecipient);
            }
        }

    }

    /**
     * If the recipient hasn't been wished yet, create a birthday email and send
     * it.
     * 
     * @param mailRecipient The recipient of the email
     */
    public void wishForBirthday(MailRecipient mailRecipient) {
        if (!alreadyWished(mailRecipient)) {
            Email email = mediator.createBirthdayEmail(mailRecipient);
            mediator.sendEmail(email, true);
        }
    }

    /**
     * It returns a list of recipients whose birthday is on the given date
     * 
     * @param date the date to check for birthdays
     * @return A list of MailRecipient objects.
     */
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

    /**
     * If the birthday is already in the HashMap, add the email to the list of
     * emails for that birthday.
     * Otherwise, create a new list with the email and add it to the HashMap
     * 
     * @param birthday    LocalDate
     * @param emailString "test@test.com"
     */
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

    /**
     * If the recipient has already received a birthday email, then return true,
     * else return false.
     * 
     * @param mailRecipient The recipient of the email
     * @return A boolean value.
     */
    public boolean alreadyWished(MailRecipient mailRecipient) {
        IHasBirthday hasBirthdayRecipient = (IHasBirthday) mailRecipient;
        List<Email> sentEmails = mediator.getSentEmailsOnDate(LocalDate.now());
        if (sentEmails != null) {
            // It's checking if the recipient has already received a birthday email.
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
