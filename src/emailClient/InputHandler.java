package emailClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

/**
 * A class that "handles" input. This includes validating and calling the next
 * procedure of the program
 */
public class InputHandler {

    private IMediator mediator;

    public InputHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * It takes a string input, checks whether it's a valid input, and if it is, it
     * creates a recipient
     * object and optionally, calls a {@link FileHandler} to write it to a file
     * 
     * @param input       The input string that the user has entered
     * @param writeToFile boolean
     * @return A boolean value.
     */
    public boolean createRecipientHandle(String input, boolean writeToFile) {

        String[] inputArray = input.trim().split("\\s*:\\s*");
        String type = inputArray[0].toLowerCase();

        // regex pattern check to match the 3 types of recipients
        Pattern validTypeCheck = Pattern.compile("(?:^|\\W)(?:official|office_friend|personal)(?:$|\\W)");
        Matcher matcher = validTypeCheck.matcher(type);
        if (!matcher.find() || inputArray.length != 2) {
            return false;
        } else {
            String[] arguments = inputArray[1].split("\\s*,\\s*");

            // In this scope we know for sure the type is either official, office_friend or
            // personal. Now we can check whether the type has the required amount of
            // arguments
            boolean notValidOffice = type.equals("official") && arguments.length != 3;
            boolean notValidOfficeFriend = type.equals("office_friend") && arguments.length != 4;
            boolean notValidPersonal = type.equals("personal") && arguments.length != 4;
            if (notValidOffice) {
                System.out.println("The \"Official\" category requires 3 arguments");
                System.out.println("official: <name>, <email>,<designation>");
                System.out.println("ex: official: nimal, nimal@gmail.com, ceo");
                return false;

            } else if (notValidOfficeFriend) {
                System.out.println("The \"Official Friend\" category requires 4 arguments");
                System.out.println("Office_friend: <name>, <email>, <designation>, <birthday>");
                System.out.println("ex: Office_friend: kamal, kamal@gmail.com, clerk, 2000/12/12");
                return false;

            } else if (notValidPersonal) {
                System.out.println("The \"Personal\" category requires 4 arguments");
                System.out.println("Personal: <name>, <nick-name>, <email>, <birthday>");
                System.out.println("ex: Personal: sunil, suniya, sunil@gmail.com, 2000/10/10");
                return false;
            }

            boolean createdRecipient = mediator.createMailRecipient(type, arguments);

            if (!createdRecipient) {
                System.out.println("Invalid input!");
                return false;
            }

            if (writeToFile) {
                mediator.writeToRecipientFile(type, arguments);
            }
        }
        return true;
    }

    /**
     * It takes a string, splits it into an array, checks if the first element of
     * the array is a valid
     * email address, checks if the array has 3 elements, and then calls a mediator
     * to send an email
     * 
     * @param input "email@email.com, subject, body"
     * @return A boolean value.
     */
    public boolean sendEmailHandle(String input) {
        String[] inputArray = input.split(",", 3);
        String emailString = inputArray[0];
        InternetAddress emailAddress = InputHandler.parseEmail(emailString);

        if (emailAddress == null) {
            // System.out.println("Invalid input!");
            return false;
        }

        if (inputArray.length != 3) {
            System.out.println("Not enough arguments");
            return false;
        }

        MailRecipient recipient = mediator.getRecipientByEmail(inputArray[0]);
        Email email;
        if (recipient != null) {
            email = mediator.createEmail(recipient, inputArray[1].trim(), inputArray[2].trim());
        } else {
            email = mediator.createEmail(emailAddress, inputArray[1].trim(), inputArray[2].trim());
        }
        mediator.sendEmail(email, false);
        return true;

    }

    /**
     * It checks whether the date is valid or not
     * 
     * @param date      The date to be checked
     * @param formatter DateTimeFormatter.ofPattern("dd/MM/yyyy")
     * @return The method is returning a LocalDate object.
     */
    public static LocalDate dateValidityCheck(String date, DateTimeFormatter formatter) {
        // Checks whether date is valid
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return parsedDate;
        } catch (Exception e) {
            System.err.println("Invalid Date");
            return null;
        }
    }

    /**
     * Checks whether we already have a recipient with the same email. Since this is
     * an email client we only stop multiple recipients from having the same email
     * i.e. we may contain multiple people with the same name,designation,birthday
     * etc. Then calls the {@link #parseEmail(String)}
     * 
     * @param emailString
     */
    public InternetAddress emailValidityCheck(String emailString) {
        if (mediator.hasRecipient(emailString)) {
            System.out.println("A recipient with this email already exists!");
            return null;
        }

        return parseEmail(emailString);

    }

    /**
     * It takes a string, and returns an InternetAddress object if the string is a
     * valid email address,
     * or null if it is not
     * 
     * @param emailString The email address to be validated.
     * @return The method is returning an InternetAddress object.
     */
    public static InternetAddress parseEmail(String emailString) {
        try {
            InternetAddress emailAddress = new InternetAddress(emailString);
            emailAddress.validate();
            return emailAddress;

        } catch (Exception e) {
            System.err.println("Invalid Email");
            return null;
        }
    }
}
