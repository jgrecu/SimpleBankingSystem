package banking;

import java.util.Random;

public class CreditCard {
    private String cardNumber;
    private String cardPin;
    private final int BIN = 400000;
    private BankAccount bankAccount;
    private final Random random = new Random();

    public CreditCard() {
        this.bankAccount = new BankAccount();
        this.cardNumber = Integer.toString(BIN) + bankAccount.getAccountNumber() + 5;
        this.cardPin = generateRandomPin();
    }

    private String generateRandomPin() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomNum = random.nextInt(10);
            while (i == 0 && randomNum == 0) {
                randomNum = random.nextInt(10);
            }
            sb.append(randomNum);

        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Your card number:\n" + this.cardNumber + "\n" +
                "Your card PIN:\n" + this.cardPin + "\n";
    }

    public String getCardPin() {
        return cardPin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public long getBalance() {
        return bankAccount.getBalance();
    }
}
