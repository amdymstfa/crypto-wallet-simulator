package repository;

import util.DatabaseConnection;
import util.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base repository with common database operations
 * Implements DRY principle and Template Method pattern
 * @param <T> The entity type
 */
public abstract class BaseRepository<T> {
    
    protected final DatabaseConnection dbConnection;
    
    /**
     * Constructor
     */
    protected BaseRepository() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Abstract method to map ResultSet to entity
     * Each repository must implement its own mapping logic
     * @param rs The ResultSet
     * @return The mapped entity
     * @throws SQLException if error occurs
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    /**
     * Abstract method to get table name
     * @return The table name
     */
    protected abstract String getTableName();
    
    /**
     * Executes a query and returns a list of entities
     * @param sql The SQL query
     * @return List of entities
     * @throws SQLException if error occurs
     */
    protected List<T> executeQuery(String sql) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return mapResultSetToList(rs);
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error executing query: " + sql, e);
            throw e;
        }
    }
    
    /**
     * Executes a prepared statement query and returns a list of entities
     * @param sql The SQL query
     * @param params The parameters to set
     * @return List of entities
     * @throws SQLException if error occurs
     */
    protected List<T> executeQuery(String sql, Object... params) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            
            return mapResultSetToList(rs);
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error executing parameterized query", e);
            throw e;
        }
    }
    
    /**
     * Executes a query that returns a single entity
     * @param sql The SQL query
     * @param params The parameters
     * @return Optional containing the entity or empty
     * @throws SQLException if error occurs
     */
    protected Optional<T> executeSingleQuery(String sql, Object... params) throws SQLException {
        List<T> results = executeQuery(sql, params);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Executes an update/insert/delete query
     * @param sql The SQL query
     * @param params The parameters
     * @return Number of affected rows
     * @throws SQLException if error occurs
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setParameters(stmt, params);
            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error executing update", e);
            throw e;
        }
    }
    
    /**
     * Counts records in the table
     * @return Total count
     * @throws SQLException if error occurs
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + getTableName();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error counting records", e);
            throw e;
        }
    }
    
    /**
     * Counts records with a WHERE clause
     * @param whereClause The WHERE clause (without WHERE keyword)
     * @param params The parameters
     * @return Count of matching records
     * @throws SQLException if error occurs
     */
    protected int countWhere(String whereClause, Object... params) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + getTableName() + " WHERE " + whereClause;
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error counting with WHERE clause", e);
            throw e;
        }
    }
    
    /**
     * Checks if a record exists by ID
     * @param id The ID to check
     * @return true if exists
     * @throws SQLException if error occurs
     */
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT 1 FROM " + getTableName() + " WHERE id = ? LIMIT 1";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            LoggerUtil.logError("Error checking existence", e);
            throw e;
        }
    }
    
    /**
     * Deletes a record by ID
     * @param id The ID
     * @return true if deleted
     * @throws SQLException if error occurs
     */
    public boolean deleteById(String id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        int rows = executeUpdate(sql, id);
        
        if (rows > 0) {
            LoggerUtil.logInfo("Deleted from " + getTableName() + ": " + id.substring(0, 8));
            return true;
        }
        return false;
    }
    
    /**
     * Maps a ResultSet to a list of entities
     * @param rs The ResultSet
     * @return List of entities
     * @throws SQLException if error occurs
     */
    protected List<T> mapResultSetToList(ResultSet rs) throws SQLException {
        List<T> entities = new ArrayList<>();
        while (rs.next()) {
            entities.add(mapResultSetToEntity(rs));
        }
        return entities;
    }
    
    /**
     * Sets parameters on a prepared statement
     * @param stmt The PreparedStatement
     * @param params The parameters
     * @throws SQLException if error occurs
     */
    protected void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            
            if (param instanceof String) {
                stmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                stmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof Double) {
                stmt.setDouble(i + 1, (Double) param);
            } else if (param instanceof Long) {
                stmt.setLong(i + 1, (Long) param);
            } else if (param instanceof Timestamp) {
                stmt.setTimestamp(i + 1, (Timestamp) param);
            } else if (param instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) param);
            } else if (param == null) {
                stmt.setNull(i + 1, Types.NULL);
            } else {
                stmt.setObject(i + 1, param);
            }
        }
    }
    
    /**
     * Gets database connection
     * @return Connection
     * @throws SQLException if error occurs
     */
    protected Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }
}