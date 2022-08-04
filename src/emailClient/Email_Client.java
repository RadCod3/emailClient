package emailClient;

//Index No. 200555H

//import libraries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Email_Client {

    public static void main(String[] args) {

        // Mediator that handles communication between different objects
        IMediator mediator = new EmailClientMediator();

        // Initializing all the factories and handlers etc.
        EmailFactory emailFactory = new EmailFactory();
        EmailSender emailSender = new EmailSender(mediator);

        RecipientFactory recipientFactory = new RecipientFactory(mediator);

        SerializeHandler serializeHandler = new SerializeHandler(mediator);
        InputHandler inputHandler = new InputHandler(mediator);
        FileHandler fileHandler = new FileHandler(mediator);
        BirthdayHandler birthdayHandler = new BirthdayHandler(mediator);

        // Setting mediator fields
        mediator.setRecipientFactory(recipientFactory);
        mediator.setInputHandler(inputHandler);
        mediator.setFileHandler(fileHandler);
        mediator.setBirthdayHandler(birthdayHandler);
        mediator.setEmailSender(emailSender);
        mediator.setEmailFactory(emailFactory);
        mediator.setSerializeHandler(serializeHandler);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        fileHandler.readRecipientsFile();

        serializeHandler.deserializeEmails();

        // Wishing for people who have birthdays today is handled by a separate thread
        // since program takes ages to start otherwise
        Thread bdayThread = new Thread(birthdayHandler);
        bdayThread.start();

        Scanner scanner = new Scanner(System.in);
        String menu = "Enter option type: \n"
                + "1 - Add new recipient\n"
                + "2 - Send email\n"
                + "3 - Print out all the recipients who have birthdays on a particular date\n"
                + "4 - Print out details of all the emails sent on a particular date\n"
                + "5 - Print out the number of recipients\n"
                + "0 - Exit Program";

        boolean exitProgram = false;
        System.out.println(menu);

        while (!exitProgram) {

            int option;
            try {
                option = scanner.nextInt();
            } catch (InputMismatchException e) {
                option = 99;
            }

            scanner.nextLine();
            switch (option) {
                case 1: {
                    // This is the code for the 1st option in the menu. It is used to add a new
                    // recipient.
                    System.out.println("Enter recipient to be added");
                    System.out.println("Recipients can be added to 3 categories");
                    System.out.println("Official, Official_Friend and Personal");
                    System.out.println("Format :- Category: Other arguments");
                    System.out.println("To get more information about each categories arguments,");
                    System.out.println("Input :- <category>: followed by any string without commas");
                    System.out.println("ex:\t Official:help\tpersonal:help");

                    String input = scanner.nextLine();
                    boolean noError = inputHandler.createRecipientHandle(input, true);
                    if (noError) {
                        System.out.println("Successful added recipient!");

                    }

                    break;
                }
                case 2: {
                    // This is the code for the 2nd option in the menu. It is used to send an email.

                    try {
                        // Waiting for the thread to finish before continuing.
                        bdayThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Input email to be added");
                    System.out.println("Format :- email, subject, content");

                    String input = scanner.nextLine().trim();
                    boolean noError = inputHandler.sendEmailHandle(input);
                    if (!noError) {
                        System.out.println("Invalid input!");
                    }
                    break;
                }
                case 3: {
                    // This is the code for the 3rd option in the menu. It is used to print out the
                    // recipients who have birthdays on the given date.
                    System.out.println("Input date of birthday");
                    System.out.println("Format :- yyyy/MM/dd (ex: 2018/09/17)");

                    String dateString = scanner.nextLine();
                    LocalDate date = InputHandler.dateValidityCheck(dateString, formatter);
                    if (date != null) {

                        List<MailRecipient> bdayRecipientList = birthdayHandler.getWishable(date);

                        if (bdayRecipientList == null) {
                            System.out.println("No recipient with a birthday on " + date);
                        } else {
                            System.out.println("People who have their birthdays on " + date + ",");

                            for (MailRecipient mailRecipient : bdayRecipientList) {
                                IHasBirthday bdayMailRecipient = (IHasBirthday) mailRecipient;
                                System.out.println(mailRecipient.getName() + " - "
                                        + mailRecipient.getEmail() + " - "
                                        + bdayMailRecipient.getAgeOn(date));
                            }
                        }
                    }

                    break;
                }
                case 4: {
                    // This is the code for the 4th option in the menu. It is used to print out the
                    // details of all the emails sent on the given date.
                    System.out.println("Input date to list emails");
                    System.out.println("Format :- yyyy/MM/dd (ex: 2018/09/17)");
                    String input = scanner.nextLine();
                    LocalDate date = InputHandler.dateValidityCheck(input, formatter);
                    if (date != null) {
                        emailSender.printSentEmailDetails(date);
                    }
                    break;
                }
                case 5: {
                    // This is the code for the 5th option in the menu. It prints out the number of
                    // total recipients in the client. Also prints out the number of each type of
                    // recipient.

                    int[] counts = RecipientFactory.getRecipientCount();
                    System.out.println(
                            "You currently have " + counts[0]
                                    + " recipients saved in this client.");
                    System.out.println("Out of which you have, " + counts[1] + " Official, " + counts[2]
                            + " Official Friend and " + counts[3] + " Personal recipients.");
                    break;
                }
                case 0:
                    // This is the code for the 0th option in the menu. It is used to exit the
                    // program.

                    System.out.println("Exiting Email Client.... have a nice day! ");
                    exitProgram = true;
                    break;

                default:
                    System.err.println("Invalid Option");
                    System.out.println(menu);

            }
            if (!exitProgram) {
                System.out.println("==============================================================");
                System.out.println("Select an Option");
            }
        }
        scanner.close();

    }

}
