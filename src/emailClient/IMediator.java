package emailClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.InternetAddress;

// The interface for the Mediator class.
public interface IMediator {
    public boolean createRecipientHandle(String input, boolean writeToFile);

    public boolean createMailRecipient(String type, String[] arguments);

    public void addToBdayTable(LocalDate birthday, String emailString);

    public void writeToRecipientFile(String type, String[] arguments);

    public InternetAddress emailValidityCheck(String emailString);

    public boolean hasRecipient(String emailString);

    public List<MailRecipient> getWishable(LocalDate date);

    public MailRecipient getRecipientByEmail(String emailString);

    public void setBirthdayHandler(BirthdayHandler birthdayHandler);

    public void setEmailFactory(EmailFactory emailFactory);

    public void setEmailSender(EmailSender emailSender);

    public void setFileHandler(FileHandler fileHandler);

    public void setInputHandler(InputHandler inputHandler);

    public void setRecipientFactory(RecipientFactory recipientFactory);

    public void setSerializeHandler(SerializeHandler serializeHandler);

    public boolean sendEmail(Email email, boolean silentSend);

    public Email createEmail(MailRecipient mailRecipient, String subject, String content);

    public Email createEmail(InternetAddress emailAddress, String subject, String content);

    public Email createBirthdayEmail(MailRecipient mailRecipient);

    public HashMap<LocalDate, List<Email>> getEmailsByDateHashMap();

    public void setEmailsByDate(HashMap<LocalDate, List<Email>> emailsByDate);

    public void serializeEmails();

    public List<Email> getSentEmailsOnDate(LocalDate date);
}
