package banking;

import java.sql.*;

public class SQLiteDB {

        private final String JDPC_URL;
        private boolean hasData = false;

        public SQLiteDB(String dbFile) {
                this.JDPC_URL = "jdbc:sqlite:" + dbFile;
                this.initialize();
        }

        private Connection connect() {
                Connection connection = null;
                try {
                        connection = DriverManager.getConnection(JDPC_URL);
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return connection;
        }

        private void initialize() {
                if (!hasData) {
                        hasData = true;

                        try (Connection connection = this.connect();
                             Statement statement = connection.createStatement()) {
                                try (ResultSet resultSet = statement.executeQuery(
                                        "SELECT name FROM sqlite_master WHERE type='table' AND name='card'")) {
                                        if (!resultSet.next()) {
                                                try (Statement statement2 = connection.createStatement()) {
                                                        statement2.execute("CREATE TABLE IF NOT EXISTS card (" +
                                                                "id INTEGER PRIMARY KEY," +
                                                                "number TEXT NOT NULL," +
                                                                "pin TEXT NOT NULL," +
                                                                "balance INTEGER DEFAULT 0" +
                                                                ");");
                                                }
                                        }
                                }
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                        }
                }
        }

        public void addCard(String cardNumber, String pin) {
                try (Connection connection = connect();
                     PreparedStatement prep = connection.prepareStatement(
                        "INSERT INTO card ('number','pin') VALUES (?,?);")) {
                        prep.setString(1, cardNumber);
                        prep.setString(2, pin);
                        prep.execute();
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
        }

        public long getBalance(String cardNumber) {
                String sql = "SELECT balance FROM card WHERE number=?";
                try (Connection connection = this.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, cardNumber);
                        try (ResultSet resultSet = pstmt.executeQuery()) {
                                if (resultSet.next()) {
                                        return resultSet.getLong("balance");
                                }
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return -1;
        }

        public boolean addBalance(String cardNumber, String sum) {
                String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
                try (Connection connection = this.connect()) {
                     try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                             pstmt.setString(1, sum);
                             pstmt.setString(2, cardNumber);
                             pstmt.executeUpdate();
                             return true;
                     }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return false;
        }

        public boolean transferAmount(String cardNumber1, String cardNumber2, String sum) {
                String removeAmount = "UPDATE card SET balance = balance - ? WHERE number = ?";
                String addAmount = "UPDATE card SET balance = balance + ? WHERE number = ?";
                try (Connection connection = this.connect()) {
                        // Disable auto-commit mode
                        connection.setAutoCommit(false);

                     try (PreparedStatement pstmt1 = connection.prepareStatement(removeAmount);
                          PreparedStatement pstmt2 = connection.prepareStatement(addAmount)) {
                             // Remove the amount from sender's account
                             pstmt1.setString(1, sum);
                             pstmt1.setString(2, cardNumber1);
                             pstmt1.executeUpdate();

                             // Add the amount to receiver's account
                             pstmt2.setString(1, sum);
                             pstmt2.setString(2, cardNumber2);
                             pstmt2.executeUpdate();

                             connection.commit();
                             return true;
                     }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return false;
        }

        public boolean checkCredentials(String cardNumber, String pin) {
                String sql = "SELECT number, pin FROM card WHERE number=? AND pin=?";
                try (Connection connection = this.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, cardNumber);
                        pstmt.setString(2, pin);
                        try (ResultSet resultSet = pstmt.executeQuery()) {
                                if (resultSet.next()) {
                                        return true;
                                }
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return false;
        }

        public boolean checkCardExists(String cardNumber) {
                String sql = "SELECT number, pin FROM card WHERE number=?";
                try (Connection connection = this.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, cardNumber);
                        try (ResultSet resultSet = pstmt.executeQuery()) {
                                if (resultSet.next()) {
                                        return true;
                                }
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return false;
        }

        public boolean closeAccount(String creditCard) {
                String sql = "DELETE FROM card WHERE number = ?";
                try (Connection connection = this.connect()) {
                        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                                pstmt.setString(1, creditCard);
                                pstmt.executeUpdate();
                                return true;
                        }
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
                return false;
        }
}
