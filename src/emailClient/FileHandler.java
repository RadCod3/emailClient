package emailClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The FileHandler class as the name suggests "Handles" things related to Files.
 */
public class FileHandler {

    private IMediator mediator;

    public FileHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * This function reads a file and creates a recipient object for each line in
     * the file
     */
    public void readRecipientsFile() {
        File recipientFile = new File("data/recipient.txt");
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

    /**
     * It writes new recipient input to the recipients file
     * 
     * @param type      String
     * @param arguments String[]
     */
    public void writeToRecipientFile(String type, String[] arguments) {
        try {
            File recipientFile = new File("data/recipient.txt");
            FileWriter fileWriter = new FileWriter(recipientFile, true);
            BufferedWriter Writer = new BufferedWriter(fileWriter);
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
