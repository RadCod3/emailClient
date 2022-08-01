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
        // TODO Write JavaDoc and regular comments

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
        // birthdayHandler.checkForWishableToday();
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
                    // input format - Official: nimal,nimal@gmail.com,ceo
                    // Use a single input to get all the details of a recipient
                    // code to add a new recipient
                    // store details in clientList.txt file
                    // Hint: use methods for reading and writing files

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
                    // input format - email, subject, content
                    // code to send an email
                    try {
                        bdayThread.join();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
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
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print recipients who have birthdays on the given date
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
                    } else {
                        System.out.println("Invalid date!");
                    }

                    break;
                }
                case 4: {
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print the details of all the emails sent on the input date
                    String input = scanner.nextLine();
                    LocalDate date = InputHandler.dateValidityCheck(input, formatter);
                    emailSender.printSentEmailDetails(date);
                    break;
                }
                case 5: {
                    int[] counts = RecipientFactory.getRecipientCount();
                    System.out.println(
                            "You currently have " + counts[0]
                                    + " recipients saved in this client.");
                    System.out.println("Out of which you have, " + counts[1] + " Official, " + counts[2]
                            + " Official Friend and " + counts[3] + " Personal recipients.");
                    break;
                }
                case 0:
                    System.out.println("Exiting email.... client have a nice day! ");
                    exitProgram = true;
                    break;

                default:
                    System.err.println("Invalid Option");
                    System.out.println(menu);

            }
        }
        scanner.close();

        // start email client
        // code to create objects for each recipient in clientList.txt
        // use necessary variables, methods and classes

    }

}

// create more classes needed for the implementation (remove the public access
// modifier from classes when you submit your code)
