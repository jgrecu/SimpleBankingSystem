package banking;

import java.sql.*;

public class SQLiteDB {

        private final String JDPC_URL;
        private static Connection connection;
        private static boolean hasData = false;

        public SQLiteDB(String dbFile) {
                this.JDPC_URL = "jdbc:sqlite:" + dbFile;
        }

        public ResultSet displayCards() throws SQLException, ClassNotFoundException {
                if (connection == null) {
                        getConnection();
                }

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM card");
                return resultSet;
        }

        private void getConnection() throws ClassNotFoundException, SQLException {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(JDPC_URL);
                initialize();
        }

        private void initialize() throws SQLException {
                if (!hasData) {
                        hasData = true;

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(
                                "SELECT name FROM sqlite_master WHERE type='table' AND name='card'");
                        if (!resultSet.next()) {
                                Statement statement2 = connection.createStatement();
                                statement2.execute("CREATE TABLE card (" +
                                        "id INTEGER PRIMARY KEY," +
                                        "number TEXT NOT NULL," +
                                        "pin TEXT NOT NULL," +
                                        "balance INTEGER DEFAULT 0" +
                                        ");");
                        }
                }
        }

        public void addCard(String cardNumber, String pin) throws SQLException, ClassNotFoundException {
                if (connection == null) {
                        getConnection();
                }
                PreparedStatement prep = connection.prepareStatement(
                        "INSERT INTO card ('number','pin') VALUES (?,?);");
                prep.setString(1, cardNumber);
                prep.setString(2, pin);
                prep.execute();
        }
}
