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

public class SerializeHandler {
    private IMediator mediator;

    public SerializeHandler(IMediator mediator) {
        this.mediator = mediator;
    }

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

    public void updateRecipientOfEmails(List<Email> emailList) {
        for (Email email : emailList) {
            String emailString = email.getRecipientEmailAddress().toString();
            if (mediator.hasRecipient(emailString)) {
                email.setRecipient(mediator.getRecipientByEmail(emailString));
            }
        }
    }

}
