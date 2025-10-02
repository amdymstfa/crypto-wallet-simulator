package repository ;

import model.Transaction;
import enums.TransactionStatus;
import util.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MempoolRepository extends BaseRepository<Transaction> {

    private final TransactionRepository transactionRepository ;

    public MempoolRepository(){
        super();
        this.transactionRepository = new TransactionRepository();
    }

    @Override
    protected String getTableName() {
        return "transactions";
    }

    @Override
    protected Transaction mapResultSetToEntity(ResultSet rs) throws SQLException {
        return transactionRepository.mapResultSetToEntity(rs);
    }

     public List<Transaction> findAllPendingOrderedByFees() throws SQLException {
        String sql = "SELECT * FROM transactions WHERE status = ? ORDER BY fees DESC, created_at ASC";
        return executeQuery(sql, TransactionStatus.PENDING.name());
    }

    public int countPendingTransactions() throws SQLException {
        return countWhere("status = ?", TransactionStatus.PENDING.name());
    }

     public double[] getMempoolStats() throws SQLException {
        String sql = "SELECT MIN(fees) as min_fees, MAX(fees) as max_fees, AVG(fees) as avg_fees " +
                    "FROM transactions WHERE status = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, TransactionStatus.PENDING.name());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new double[] {
                    rs.getDouble("min_fees"),
                    rs.getDouble("max_fees"),
                    rs.getDouble("avg_fees")
                };
            }
            
            return new double[] { 0.0, 0.0, 0.0 };
        }
    }

     public int getTransactionPosition(String transactionId) throws SQLException {
        String sql = "WITH ranked_transactions AS (" +
                    "  SELECT id, ROW_NUMBER() OVER (ORDER BY fees DESC, created_at ASC) as position " +
                    "  FROM transactions WHERE status = ?" +
                    ") " +
                    "SELECT position FROM ranked_transactions WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, TransactionStatus.PENDING.name());
            stmt.setString(2, transactionId);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next() ? rs.getInt("position") : -1;
        }
    }

     public List<String> confirmTopTransactions(int count) throws SQLException {
        String selectSql = "SELECT id FROM transactions WHERE status = ? " +
                          "ORDER BY fees DESC, created_at ASC LIMIT ?";
        String updateSql = "UPDATE transactions SET status = ? WHERE id = ?";
        
        List<String> confirmedIds = new ArrayList<>();
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                 PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                
                selectStmt.setString(1, TransactionStatus.PENDING.name());
                selectStmt.setInt(2, count);
                ResultSet rs = selectStmt.executeQuery();
                
                while (rs.next()) {
                    String txId = rs.getString("id");
                    updateStmt.setString(1, TransactionStatus.CONFIRMED.name());
                    updateStmt.setString(2, txId);
                    updateStmt.executeUpdate();
                    confirmedIds.add(txId);
                }
                
                conn.commit();
                LoggerUtil.logInfo("Confirmed " + confirmedIds.size() + " transactions");
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        
        return confirmedIds;
    }
    
}