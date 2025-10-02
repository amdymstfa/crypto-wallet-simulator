package repository;

import model.*;
import enums.CryptoType;
import util.LoggerUtil;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class WalletRepository extends BaseRepository<Wallet> {

    @Override
    public String getTableName(){
        return "Wallets";
    }

//     @Override
//     protected Wallet mapResultSetToEntity(ResultSet rs) throws SQLException {
//     String id = rs.getString("id");
//     String address = rs.getString("address");
//     String cryptoTypeStr = rs.getString("crypto_type");
//     double balance = rs.getDouble("balance");

    
//     CryptoType cryptoType = CryptoType.valueOf(cryptoTypeStr.toUpperCase());

//     Wallet wallet = (cryptoType == CryptoType.BITCOIN) ? 
//         new BitcoinWallet(id, address) : new EthereumWallet(id, address);

//     wallet.updateBalance(balance);
//     return wallet;
// }

    @Override
    protected Wallet mapResultSetToEntity(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String address = rs.getString("address");
        String cryptoTypeStr = rs.getString("crypto_type");
        double balance = rs.getDouble("balance");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        CryptoType cryptoType = CryptoType.valueOf(cryptoTypeStr.toUpperCase());

        return (cryptoType == CryptoType.BITCOIN)
            ? new BitcoinWallet(id, address, balance, createdAt)
            : new EthereumWallet(id, address, balance, createdAt);
    }


    public void save(Wallet wallet) throws SQLException {
        String sql = "INSERT INTO wallets (id, address, crypto_type, balance, created_at) " +
                    "VALUES (?, ?, ?, ?, ?)";

        int rows = executeUpdate(sql,
            wallet.getId(),
            wallet.getAddress(),
            wallet.getType().name(),
            wallet.getBalance(),
            Timestamp.valueOf(wallet.getCreatedAt())
        );

        if (rows > 0){
            LoggerUtil.logInfo("wallet saved : " + wallet.getId().substring(0,8));
        }
    }

    // find by id
    public Optional<Wallet> findById(String id)throws SQLException {
        String sql = "SELECT * FROM wallets where id = ?";
        return executeSingleQuery(sql, id);
    }

    // find wallet bu address
    public Optional<Wallet> findByAddress(String address) throws SQLException{
        String sql = "SELECT * FROM wallets WHERE address = ?";
        return executeSingleQuery(sql, address);
    }

    // find all wallets 
   public List<Wallet> findAll() throws SQLException {
        String sql = "SELECT * FROM wallets ORDER BY created_at DESC";
        return executeQuery(sql);
    }

    // find wallets by type
     public List<Wallet> findByType(CryptoType type) throws SQLException {
        String sql = "SELECT * FROM wallets WHERE crypto_type = ? ORDER BY created_at DESC";
        return executeQuery(sql, type.name());
    }

    // update balance 
      public void updateBalance(String id, double newBalance) throws SQLException {
        String sql = "UPDATE wallets SET balance = ? WHERE id = ?";
        int rows = executeUpdate(sql, newBalance, id);
        
        if (rows > 0) {
            LoggerUtil.logInfo("Balance updated for: " + id.substring(0, 8));
        }
    }
}
 