DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS wallets CASCADE;

-- Wallets table
CREATE TABLE wallets (
    id VARCHAR(36) PRIMARY KEY,
    address VARCHAR(100) NOT NULL UNIQUE,
    crypto_type VARCHAR(20) NOT NULL CHECK (crypto_type IN ('BITCOIN', 'ETHEREUM')),
    balance DECIMAL(20, 8) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_balance_positive CHECK (balance >= 0)
);

CREATE INDEX idx_wallets_address ON wallets(address);
CREATE INDEX idx_wallets_crypto_type ON wallets(crypto_type);

-- Transactions table
CREATE TABLE transactions (
    id VARCHAR(36) PRIMARY KEY,
    from_address VARCHAR(100) NOT NULL,
    to_address VARCHAR(100) NOT NULL,
    amount DECIMAL(20, 8) NOT NULL,
    fees DECIMAL(20, 8) NOT NULL DEFAULT 0.0,
    crypto_type VARCHAR(20) NOT NULL CHECK (crypto_type IN ('BITCOIN', 'ETHEREUM')),
    fee_level VARCHAR(20) NOT NULL CHECK (fee_level IN ('ECONOMIQUE', 'STANDARD', 'RAPIDE')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_fees_positive CHECK (fees >= 0),
    CONSTRAINT chk_different_addresses CHECK (from_address != to_address)
);

CREATE INDEX idx_transactions_from_address ON transactions(from_address);
CREATE INDEX idx_transactions_to_address ON transactions(to_address);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_crypto_type ON transactions(crypto_type);
CREATE INDEX idx_transactions_created_at ON transactions(created_at DESC);

-- Seed data
INSERT INTO wallets (id, address, crypto_type, balance) VALUES 
('test-btc-001', '1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa', 'BITCOIN', 0.5),
('test-eth-001', '0x742d35Cc6634C0532925a3b8D9f4e676C2fC2f36', 'ETHEREUM', 10.0);

-- Views
CREATE OR REPLACE VIEW v_transaction_stats AS
SELECT 
    crypto_type,
    status,
    COUNT(*) AS count,
    SUM(amount) AS total_amount,
    SUM(fees) AS total_fees,
    AVG(fees) AS avg_fees
FROM transactions
GROUP BY crypto_type, status;

CREATE OR REPLACE VIEW v_mempool AS
SELECT 
    id,
    from_address,
    to_address,
    amount,
    fees,
    crypto_type,
    fee_level,
    created_at
FROM transactions
WHERE status = 'PENDING'
ORDER BY fees DESC, created_at ASC;

-- Comments
COMMENT ON TABLE wallets IS 'Crypto wallets storage';
COMMENT ON TABLE transactions IS 'Crypto transactions storage';
COMMENT ON COLUMN wallets.balance IS 'Balance in crypto units (BTC or ETH)';
COMMENT ON COLUMN transactions.fees IS 'Transaction fees in crypto units';
COMMENT ON VIEW v_mempool IS 'Mempool view (pending transactions ordered by fees)';
