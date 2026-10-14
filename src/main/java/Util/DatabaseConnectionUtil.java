package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * @author Gracielle Salvador
 */

/**
 * DatabaseConnection is a utility class that manages the connection to a MySQL database.
 * It reads database credentials from a properties file and provides methods to establish
 * and close the database connection.
 */
public class DatabaseConnectionUtil {

    // Static variable to hold the single instance of the database connection
    private static Connection connection = null;

    /**
     * Gets the database connection. If the connection does not already exist,
     * it will be created by loading the database credentials from the properties file.
     *
     * @return the database connection, or null if the connection could not be established
     */
    public static Connection getConnection() {
        try {
            // Check if connection is null, closed, or invalid
            if (connection == null || connection.isClosed() || !connection.isValid(5)) {
                // Load properties file containing database credentials
                Properties properties = new Properties();
                try (InputStream input = DatabaseConnectionUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
                    if (input == null) {
                        System.out.println("Sorry, unable to find database.properties");
                        return null;
                    }
                    properties.load(input);
                }

                // Retrieve database credentials from the properties file
                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.user");
                String password = properties.getProperty("db.password");

                // Establish the connection to the database
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connected successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

     /**
     * NEW: Gets a database connection specifically for testing purposes using an in-memory H2 database.
     * This method does not use the properties file.
     *
     * @param jdbcUrl The JDBC URL for the H2 in-memory database (e.g., "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1").
     * @return A new Connection object to the H2 database.
     * @throws SQLException If a database access error occurs or the URL is null.
     */
    public static Connection getTestConnection(String jdbcUrl) throws SQLException {
        // For H2, "sa" is the default user with no password
        // Class.forName("org.h2.Driver"); // Often not needed, DriverManager finds it if on classpath
        return DriverManager.getConnection(jdbcUrl, "sa", "");
    }
    
    /**
     * Closes the database connection if it is currently open.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Reset to null so next getConnection() creates a new one
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
