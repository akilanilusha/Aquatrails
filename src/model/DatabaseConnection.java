package model;
import java.sql.*;

public class DatabaseConnection {

    // Database credentials and settings
    private static final String DATABASE_NAME = "AquaTrails";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    // Get the database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database: " + DATABASE_NAME);
            } catch (Exception e) {
                System.out.println("Connection Error: " + e.getMessage());
            }
        }
        return connection;
    }

    // Create the AquaTrails database if it doesn't exist
    public static void createDatabase() {
        String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDbQuery);
            System.out.println("Database '" + DATABASE_NAME + "' created or already exists.");
        } catch (SQLException e) {
            System.out.println("Database creation failed: " + e.getMessage());
        }
    }

    // Create table
    public static void createTable(String createTableQuery) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(createTableQuery);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    // Insert data
    public static void insertData(String insertQuery) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(insertQuery);
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Data insertion failed: " + e.getMessage());
        }
    }

    // Update data
    public static void updateData(String updateQuery) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println("Data updated successfully.");
        } catch (SQLException e) {
            System.out.println("Data update failed: " + e.getMessage());
        }
    }

    // Search data (returns ResultSet)
    public static ResultSet searchData(String searchQuery) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(searchQuery);
        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
            return null;
        }
    }

    // Delete data
    public static void deleteData(String deleteQuery) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(deleteQuery);
            System.out.println("Data deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    // Close connection
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Connection close failed: " + e.getMessage());
        }
    }
}
