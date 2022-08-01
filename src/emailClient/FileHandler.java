package emailClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {

    private IMediator mediator;

    public FileHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    public void readRecipientsFile() {
        File recipientFile = new File("data/recipient_v2.txt");
        if (recipientFile.exists()) {
            try {
                Scanner fileReader = new Scanner(recipientFile);
                while (fileReader.hasNextLine()) {
                    String input = fileReader.nextLine();
                    mediator.createRecipientHandle(input, false);
                }
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToRecipientFile(String type, String[] arguments) {
        try {
            File recipientFile = new File("data/recipient_v2.txt");
            FileWriter Writer = new FileWriter(recipientFile, true);
            if (!recipientFile.exists()) {
                recipientFile.createNewFile();
            }

            Writer.write(type + ':');
            for (int i = 0; i < arguments.length; i++) {
                if (!(i == arguments.length - 1)) {
                    Writer.write(arguments[i] + ',');
                } else {
                    Writer.write(arguments[i] + '\n');
                }
            }

            Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
