package emailClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The SerializeHandler class as the name suggests "Handles" things related to
 * serializing. This includes Serializing,Deserializing etc.
 */
public class SerializeHandler {
    private IMediator mediator;

    public SerializeHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * It takes the emailsByDate HashMap and serializes it to a file.
     */
    public void serializeEmails() {
        try {
            FileOutputStream serializedFile = new FileOutputStream("data/emailsByDate.ser");
            ObjectOutputStream out = new ObjectOutputStream(serializedFile);

            out.writeObject(mediator.getEmailsByDateHashMap());

            out.close();
            serializedFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It reads a serialized HashMap from a file, updates the recipient of each
     * email in the HashMap,
     * and then sets the HashMap as the value of a field in the EmailFactory
     */
    public void deserializeEmails() {
        try {
            FileInputStream serializedFile = new FileInputStream("data/emailsByDate.ser");
            ObjectInputStream in = new ObjectInputStream(serializedFile);

            @SuppressWarnings("unchecked")
            HashMap<LocalDate, List<Email>> emailsByDate = (HashMap<LocalDate, List<Email>>) in.readObject();

            in.close();
            serializedFile.close();

            Set<LocalDate> setOfKeys = emailsByDate.keySet();

            for (LocalDate localDate : setOfKeys) {
                updateRecipientOfEmails(emailsByDate.get(localDate));
            }

            mediator.setEmailsByDate(emailsByDate);

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function takes a list of emails and if the email's recipient
     * emailAddress has a recipient saved, updates it
     *
     * @param emailList List of Email objects
     */
    public void updateRecipientOfEmails(List<Email> emailList) {
        for (Email email : emailList) {
            String emailString = email.getRecipientEmailAddress().toString();
            if (mediator.hasRecipient(emailString)) {
                email.setRecipient(mediator.getRecipientByEmail(emailString));
            }
        }
    }

}
