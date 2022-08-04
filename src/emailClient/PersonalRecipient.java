package emailClient;

import java.time.LocalDate;
import java.time.Period;

import javax.mail.internet.InternetAddress;

/**
 * A PersonalRecipient is a MailRecipient that has a nickname and a birthday. It
 * represents a friend
 */
public class PersonalRecipient extends MailRecipient implements IHasBirthday {

    private String nickName;
    private LocalDate birthday;
    private static int count = 0;

    public PersonalRecipient(String name, String nickName, InternetAddress emailAddress, LocalDate birthday) {
        super(name, emailAddress);
        this.nickName = nickName;
        this.birthday = birthday;
        count++;
    }

    @Override
    public String wishForBirthdayContent() {
        String content = "Hugs and love for your birthday\n♪o<( ´∀｀)っ┌iii┐";
        return content;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String wishForBdaySubject() {
        return String.format("Happy Birthday %s", nickName);
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
