package emailClient;

import java.time.LocalDate;

public interface IHasBirthday {
    public LocalDate getBirthday();

    public String wishForBirthdayContent();

    public String wishForBdaySubject();
}
