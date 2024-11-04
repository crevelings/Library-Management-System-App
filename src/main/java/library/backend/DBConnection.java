package library.backend;

import java.sql.*;

public class DBConnection {
    private static final String BOOKS_DB_URL = "jdbc:mysql://localhost:3306/Library";
    private static final String DB_USER = "crevelings";
    private static final String DB_PASSWORD = "Goeagles";

    public static Connection getLibraryConnection() throws SQLException {
        return DriverManager.getConnection(BOOKS_DB_URL, DB_USER, DB_PASSWORD);
    }
}
