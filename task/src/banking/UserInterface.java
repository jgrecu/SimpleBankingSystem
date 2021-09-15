package banking;

import java.util.Scanner;

public class UserInterface {

    final Scanner scanner = new Scanner(System.in);
    final String dbFile;
    final SQLiteDB dbCon;

    public UserInterface(String dbFile) {
        this.dbFile = dbFile;
        this.dbCon = new SQLiteDB(dbFile);
    }

    private void printMainMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    public void processMain() {
        boolean noBreak = true;
        while (noBreak) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addNewCard();
                    break;
                case "2":
                    processLogin();
                    break;
                case "0":
                    noBreak = false;
                    break;
                default:
                    break;
            }
        }
        System.out.println("\nBye!");
    }

    private void addNewCard() {
        CreditCardAccount card = new CreditCardAccount();
        dbCon.addCard(card.getCardNumber(), card.getCardPin());
        System.out.println("\nYour card has been created");
        System.out.println(card);
    }

    private void processLogin() {
        System.out.println("\nEnter your card number:");
        String creditCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        boolean validCard = dbCon.checkCredentials(creditCard, pin);
        if (validCard) {
            System.out.println("\nYou have successfully logged in!\n");
            processUser(creditCard);
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void processUser(String creditCard) {
        boolean noBreak = true;
        while (noBreak) {
            printUserMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\nBalance: " + dbCon.getBalance(creditCard) + "\n");
                    break;
                case "2":
                    addIncome(creditCard);
                    break;
                case "3":
                    transferAmount(creditCard);
                    break;
                case "4":
                    deleteCard(creditCard);
                    noBreak = false;
                    break;
                case "5":
                    System.out.println("\nYou have successfully logged out!\n");
                    noBreak = false;
                    break;
                case "0":
                    noBreak = false;
                    System.out.println("\nBye!");
                    System.exit(1);
                    break;
                default:
                    break;
            }
        }
    }

    private void transferAmount(String creditCard) {
        System.out.println("\nTransfer\n" +
                "Enter card number:");
        String creditCard2 = scanner.nextLine();
        boolean isCardValid = CreditCardAccount.validateCreditCard(creditCard2);
        if (isCardValid) {
            if (dbCon.checkCardExists(creditCard2)) {
                System.out.println("Enter how much money you want to transfer:");
                String amount = scanner.nextLine();
                long longAmount = Long.parseLong(amount);
                if (dbCon.getBalance(creditCard) >= longAmount) {
                    dbCon.transferAmount(creditCard, creditCard2, amount);
                    System.out.println("Success!\n");
                } else {
                    System.out.println("Not enough money!\n");
                }
            } else {
                System.out.println("Such a card does not exist.\n");
            }
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
        }
    }

    private void deleteCard(String creditCard) {
        if (dbCon.closeAccount(creditCard)) {
            System.out.println("The account has been closed!\n");
        }
    }

    private void printUserMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    private void addIncome(String creditCard) {
        System.out.println("\nEnter income:");
        String amount = scanner.nextLine();
        boolean successfulTransaction = dbCon.addBalance(creditCard, amount);
        if (successfulTransaction) {
            System.out.println("Income was added!\n");
        }
    }
}
