package banking;

import java.util.Random;

public class CreditCardAccount {
    private final Random random = new Random();
    private final int BIN = 400000;
    private String cardNumber;
    private String cardPin;
    private String bankAccount;
    private long balance;

    public CreditCardAccount() {
        this.bankAccount = generateAccountNumber();
        this.cardNumber = generateCardNumber();
        this.cardPin = generateRandomPin();
        this.balance = 0;
    }

    private String generateAccountNumber() {
        //return String.format("%09d", random.nextInt(1000000000));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            int randomNum = random.nextInt(10);
            sb.append(randomNum);

        }
        return sb.toString();
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

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append(BIN).append(bankAccount);
        int checksum = calculateChecksum(sb.toString());
        sb.append(checksum);
        return sb.toString();
    }

    private int calculateChecksum(String cardNo) {
        // using Luhn algorithm
        int nDigits = cardNo.length();
        int nSum = 0;
        for (int i = 0; i < nDigits; i++) {
            int d = cardNo.charAt(i) - '0';
            if (i % 2 == 0) {
                d *= 2;
                nSum += d / 10;
                nSum += d % 10;
            } else {
                nSum += d;
            }
        }
        return nSum % 10 == 0 ? 0 : 10 - nSum % 10;
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

    public void withdraw(long amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public void deposit(long amount) {
        balance += amount;
    }

    public long getBalance() {
        return balance;
    }
}
