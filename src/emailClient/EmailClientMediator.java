package emailClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.InternetAddress;

public class EmailClientMediator implements IMediator {

	private RecipientFactory recipientFactory;
	private InputHandler inputHandler;
	private FileHandler fileHandler;
	private EmailFactory emailFactory;
	private EmailSender emailSender;
	private BirthdayHandler birthdayHandler;
	private SerializeHandler serializeHandler;

	public void setBirthdayHandler(BirthdayHandler birthdayHandler) {
		this.birthdayHandler = birthdayHandler;
	}

	public void setEmailFactory(EmailFactory emailFactory) {
		this.emailFactory = emailFactory;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	public void setRecipientFactory(RecipientFactory recipientFactory) {
		this.recipientFactory = recipientFactory;
	}

	public void setSerializeHandler(SerializeHandler serializeHandler) {
		this.serializeHandler = serializeHandler;
	}

	@Override
	public boolean createRecipientHandle(String input, boolean writeToFile) {
		return inputHandler.createRecipientHandle(input, writeToFile);
	}

	@Override
	public boolean createMailRecipient(String type, String[] arguments) {
		return recipientFactory.createMailRecipient(type, arguments);
	}

	@Override
	public void addToBdayTable(LocalDate birthday, String emailString) {
		birthdayHandler.addToBdayTable(birthday, emailString);

	}

	@Override
	public void writeToRecipientFile(String type, String[] arguments) {
		fileHandler.writeToRecipientFile(type, arguments);

	}

	@Override
	public InternetAddress emailValidityCheck(String emailString) {
		return inputHandler.emailValidityCheck(emailString);
	}

	@Override
	public boolean hasRecipient(String emailString) {
		return recipientFactory.hasRecipient(emailString);
	}

	@Override
	public List<MailRecipient> getWishable(LocalDate date) {
		return birthdayHandler.getWishable(date);
	}

	@Override
	public MailRecipient getRecipientByEmail(String emailString) {
		return recipientFactory.getRecipientByEmail(emailString);
	}

	@Override
	public void sendEmail(Email email, boolean silentSend) {
		emailSender.sendEmail(email, silentSend);
	}

	@Override
	public Email createBirthdayEmail(MailRecipient mailRecipient) {
		return emailFactory.createBirthdayEmail(mailRecipient);
	}

	@Override
	public Email createEmail(MailRecipient mailRecipient, String subject, String content) {
		return emailFactory.createEmail(mailRecipient, subject, content);
	}

	@Override
	public Email createEmail(InternetAddress emailAddress, String subject, String content) {
		return emailFactory.createEmail(emailAddress, subject, content);
	}

	@Override
	public HashMap<LocalDate, List<Email>> getEmailsByDateHashMap() {
		return emailSender.getEmailsByDateHashMap();
	}

	@Override
	public void setEmailsByDate(HashMap<LocalDate, List<Email>> emailsByDate) {
		emailSender.setEmailsByDate(emailsByDate);
	}

	@Override
	public void serializeEmails() {
		serializeHandler.serializeEmails();

	}

	@Override
	public List<Email> getSentEmailsOnDate(LocalDate date) {
		return emailSender.getSentEmailsOnDate(date);
	}

}
