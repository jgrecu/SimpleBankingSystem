package banking;

import java.sql.*;

public class SQLiteDB {

        private final String JDPC_URL;
        private static boolean hasData = false;

        public SQLiteDB(String dbFile) {
                this.JDPC_URL = "jdbc:sqlite:" + dbFile;
                this.initialize();
        }

        private Connection connect() {
                Connection connection = null;
                try {
                        connection = DriverManager.getConnection(JDPC_URL);
//                        initialize();
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
                                                Statement statement2 = connection.createStatement();
                                                statement2.execute("CREATE TABLE IF NOT EXISTS card (" +
                                                        "id INTEGER PRIMARY KEY," +
                                                        "number TEXT NOT NULL," +
                                                        "pin TEXT NOT NULL," +
                                                        "balance INTEGER DEFAULT 0" +
                                                        ");");
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
}
