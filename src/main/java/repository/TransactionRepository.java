package repository ;

import model.Transaction;
import enums.*;
import util.LoggerUtil;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TransactionRepository extends BaseRepository<Transaction>{

    @Override
    public String getTableName(){
        return "transactions";
    }

    @Override
    protected Transaction mapResultSetToEntity(ResultSet rs) throws SQLException {
        String fromAddress = rs.getString("from_address");
        String toAddress = rs.getString("to_address");
        double amount = rs.getDouble("amount");
        CryptoType type = CryptoType.valueOf(rs.getString("crypto_type"));
        FeeLevel feeLevel = FeeLevel.valueOf(rs.getString("fee_level"));
        
        Transaction transaction = new Transaction(fromAddress, toAddress, amount, type, feeLevel);
        transaction.setFees(rs.getDouble("fees"));
        transaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
        
        return transaction;
    }

    public void save(Transaction tx) throws SQLException {
        String sql = "INSERT INTO transactions (id, from_address, to_address, amount, fees, " +
                    "crypto_type, fee_level, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        int rows = executeUpdate(sql,
            tx.getId(), tx.getFromAddress(), tx.getToAddress(), tx.getAmount(),
            tx.getFees(), tx.getType().name(), tx.getFeeLevel().name(),
            tx.getStatus().name(), Timestamp.valueOf(tx.getCreatedAt())
        );
        
        if (rows > 0) {
            LoggerUtil.logTransaction(tx.getId(), "SAVED_TO_DB", "Success");
        }
    }

    public Optional<Transaction> findById(String id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        return executeSingleQuery(sql, id);
    }

     public List<Transaction> findAll() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        return executeQuery(sql);
    }

    public List<Transaction> findByStatus(TransactionStatus status) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE status = ? ORDER BY fees DESC";
        return executeQuery(sql, status.name());
    }

      public List<Transaction> findByType(CryptoType type) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE crypto_type = ? ORDER BY created_at DESC";
        return executeQuery(sql, type.name());
    }

    public List<Transaction> findPendingOrderedByFees() throws SQLException {
        String sql = "SELECT * FROM v_mempool";
        return executeQuery(sql);
    }

     public void updateStatus(String id, TransactionStatus newStatus) throws SQLException {
        String sql = "UPDATE transactions SET status = ? WHERE id = ?";
        int rows = executeUpdate(sql, newStatus.name(), id);
        
        if (rows > 0) {
            LoggerUtil.logTransaction(id, "STATUS_UPDATED", newStatus.name());
        }
    }

    public int countByStatus(TransactionStatus status) throws SQLException {
        return countWhere("status = ?", status.name());
    }
}