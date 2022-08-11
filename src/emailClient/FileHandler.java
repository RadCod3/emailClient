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
        Scanner fileReader = null;
        if (recipientFile.exists()) {
            try {
                fileReader = new Scanner(recipientFile);
                while (fileReader.hasNextLine()) {
                    String input = fileReader.nextLine();
                    mediator.createRecipientHandle(input, false);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception ignoreMe) {
                    }
                }
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
        FileWriter fileWriter = null;
        BufferedWriter writer = null;
        try {
            File recipientFile = new File("data/recipient.txt");
            fileWriter = new FileWriter(recipientFile, true);
            writer = new BufferedWriter(fileWriter);
            if (!recipientFile.exists()) {
                recipientFile.createNewFile();
            }

            writer.write(type + ':');
            for (int i = 0; i < arguments.length; i++) {
                if (!(i == arguments.length - 1)) {
                    writer.write(arguments[i] + ',');
                } else {
                    writer.write(arguments[i] + '\n');
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                writer = null;
                if (fileWriter != null) {
                    fileWriter.close();
                }
                fileWriter = null;

            } catch (Exception ignoreMe) {
            }

        }
    }
}
