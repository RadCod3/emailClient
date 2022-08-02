package emailClient;

import java.time.LocalDate;
import java.time.Period;

import javax.mail.internet.InternetAddress;

/**
 * It's a class that represents a friend who is also an official recipient
 */
public class OfficialFriendRecipient extends OfficialRecipient implements IHasBirthday {

    private LocalDate birthday;
    private static int count = 0;

    OfficialFriendRecipient(String name, InternetAddress emailAddress, String designation, LocalDate birthday) {
        super(name, emailAddress, designation);
        this.birthday = birthday;
        count++;
    }

    @Override
    public String wishForBirthdayContent() {
        return String.format("Wish you a happy birthday, %s", this.getName());
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String wishForBdaySubject() {
        return String.format("Happy Birthday %s", this.getName());
    }

    public static int getCount() {
        return count;
    }

    @Override
    public int getAgeOn(LocalDate date) {
        Period period = Period.between(birthday, date);
        return period.getYears();
    }

}
