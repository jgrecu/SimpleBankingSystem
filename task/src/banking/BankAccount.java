package banking;

import java.util.Random;

public class BankAccount {
    private String name;
    private long accountNumber;
    private long balance;
    private final Random random = new Random();

    public BankAccount() {
        this.name = " ";
        this.balance = 0;
        this.accountNumber = generateAccountNumber();
    }

    public BankAccount(long initialBalance, String name) {
        this.balance = initialBalance;
        this.name = name;
        this.accountNumber = generateAccountNumber();
    }

    private long generateAccountNumber() {
        String substring = String.format("%09d", random.nextInt(1000000000));
        return Long.parseLong(substring);
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

    public long getAccountNumber() {
        return accountNumber;
    }
}
