package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;

    // Database connection object
    private Connection connection;

    // Database credentials
    private static final String URL = EnvLoader.get("DB_URL");
    private static final String USER = EnvLoader.get("DB_USER");
    private static final String PASSWORD = EnvLoader.get("DB_PASSWORD");

    // Private constructor for Singleton
    private DatabaseConnection() {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish connection
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LoggerUtil.logInfo("✅ Successfully connected to PostgreSQL");

        } catch (ClassNotFoundException e) {
            LoggerUtil.logError("❌ PostgreSQL Driver not found", e);
            throw new RuntimeException("PostgreSQL driver not found. Check CLASSPATH.", e);

        } catch (SQLException e) {
            LoggerUtil.logError("❌ PostgreSQL connection error", e);
            throw new RuntimeException("Failed to connect to PostgreSQL: " + e.getMessage(), e);
        }
    }

    // Get singleton instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Get database connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            LoggerUtil.logWarning("⚠️ Connection closed, reconnecting...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LoggerUtil.logInfo("✅ Reconnection successful");
        }
        return connection;
    }

    // Close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LoggerUtil.logInfo("🔌 PostgreSQL connection closed");
            } catch (SQLException e) {
                LoggerUtil.logError("❌ Error while closing the connection", e);
            }
        }
    }

    // Test if connection is valid
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            LoggerUtil.logError("❌ Connection test failed", e);
            return false;
        }
    }

    /**
     * Utility method to create a connection with custom parameters
     */
    public static Connection createConnection(String url, String user, String password) 
            throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }
}
