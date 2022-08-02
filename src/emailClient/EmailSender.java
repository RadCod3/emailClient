package emailClient;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * The EmailSender class does the sending of emails and is the class that mainly
 * interacts with {@link javax.mail} api
 */
public class EmailSender {

    private IMediator mediator;

    private final String username = "radith.testing@gmail.com";
    private final String password = "fuzaqbzrckcqnlxf";

    // TODO Save emails despite them not getting sent
    // But that requires serializing twice does it lets see
    private HashMap<LocalDate, List<Email>> emailsByDate = new HashMap<LocalDate, List<Email>>();

    public EmailSender(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * It sends an email
     * 
     * @param email      Email object
     * @param silentSend boolean
     */
    public void sendEmail(Email email, boolean silentSend) {

        InternetAddress[] recipientAddress = { email.getRecipientEmailAddress() };

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("radith.testing@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    recipientAddress);
            message.setSubject(email.getSubject());
            message.setText(email.getContent());

            Transport.send(message);

            email.setSent();

            if (!silentSend) {
                System.out.println("Email Sent!");
            }

            if (emailsByDate.containsKey(LocalDate.now())) {
                emailsByDate.get(LocalDate.now()).add(email);
            } else {
                List<Email> emailList = new ArrayList<Email>();
                emailList.add(email);
                emailsByDate.put(LocalDate.now(), emailList);
            }
            // TODO Move serialize Emails to inputhandler or EmailClient that way we can
            // serialize only after all birthday emails are sent
            mediator.serializeEmails();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function prints the details of all the emails sent on a particular date
     * 
     * @param date The date to search for sent emails
     */
    public void printSentEmailDetails(LocalDate date) {
        List<Email> sentEmails = getSentEmailsOnDate(date);
        if (sentEmails != null) {
            for (Email email : sentEmails) {
                String recipient;
                if (email.getRecipient() == null) {
                    recipient = email.getRecipientEmailAddress().toString();
                } else {
                    recipient = email.getRecipient().getName();
                }

                System.out.println("\"" + email.getSubject() + "\" sent to " + recipient + " on "
                        + email.getSentTime().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")));
            }
        }
        if (emailsByDate.containsKey(date)) {

        } else {
            System.out.println("No emails sent on this day!");
        }
    }

    /**
     * If the emailsByDate HashMap contains the date, return the list of emails for
     * that date. Otherwise, return null
     * 
     * @param date The date to search for emails
     * @return A list of emails sent on a given date.
     */
    public List<Email> getSentEmailsOnDate(LocalDate date) {
        if (emailsByDate.containsKey(date)) {
            return emailsByDate.get(date);
        } else {
            return null;
        }
    }

    public HashMap<LocalDate, List<Email>> getEmailsByDateHashMap() {
        return emailsByDate;
    }

    public void setEmailsByDate(HashMap<LocalDate, List<Email>> emailsByDate) {
        this.emailsByDate = emailsByDate;
    }
}
