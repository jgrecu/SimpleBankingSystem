package banking;

public class Main {

    public static void main(String[] args) {

        if (args[0].equals("-fileName")) {
            String dbFile = args[1];
            UserInterface atm = new UserInterface(dbFile);
            atm.processMain();
        }
    }
}