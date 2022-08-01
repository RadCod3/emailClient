package emailClient;

import java.time.LocalDate;
import java.time.Period;

import javax.mail.internet.InternetAddress;

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
        return String.format("Hugs and love for your birthday %s", nickName);
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
        return String.format("Happy Birthday %s!!!", this.getNickName());
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
