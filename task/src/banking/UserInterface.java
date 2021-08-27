package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    final List<CreditCardAccount> creditCardAccountList;
    final Scanner scanner = new Scanner(System.in);

    public UserInterface() {
        this.creditCardAccountList = new ArrayList<>();
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
        creditCardAccountList.add(card);
        System.out.println("\nYour card has been created");
        System.out.println(card);
    }

    private void processLogin() {
        System.out.println("\nEnter your card number:");
        String creditCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        CreditCardAccount userCard = checkCredentials(creditCard, pin);
        if (userCard != null) {
            processUser(userCard);
        }
    }

    private CreditCardAccount checkCredentials(String creditCard, String pin) {
        for (CreditCardAccount card : creditCardAccountList) {
            if (card.getCardNumber().equals(creditCard) && card.getCardPin().equals(pin)) {
                System.out.println("\nYou have successfully logged in!\n");
                return card;
            }
        }
        System.out.println("\nWrong card number or PIN!\n");
        return null;
    }

    private void processUser(CreditCardAccount card) {
        boolean noBreak = true;
        while (noBreak) {
            printLogMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\nBalance: " + card.getBalance() + "\n");
                    break;
                case "2":
                    System.out.println("\nYou have successfully logged out!\n");
                    noBreak = false;
                    break;
                case "0":
                    noBreak = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void printLogMenu() {
        System.out.println("1. Balance\n" +
                "2. Log out\n" +
                "0. Exit");
    }
}
