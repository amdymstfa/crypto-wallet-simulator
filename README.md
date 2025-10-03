# Crypto Wallet Simulator

A Java 8 console application simulating a cryptocurrency wallet with mempool functionality to optimize transaction fees.

![Java](https://img.shields.io/badge/Java-8-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue)
![JDBC](https://img.shields.io/badge/JDBC-42.6.0-green)
![Status](https://img.shields.io/badge/Status-Production-success)

## Description

This project simulates a crypto wallet with a mempool (transaction queue) system. Users can create Bitcoin or Ethereum wallets, perform transactions with different fee levels, and visualize their position in the mempool to estimate confirmation time.

### Educational Objectives

- **Layered Architecture**: Presentation, Business, Data, Utility layers
- **Design Patterns**: Singleton, Repository, Strategy, Template Method, Factory
- **SOLID Principles**: Strict adherence to all 5 principles
- **PostgreSQL/JDBC**: Data persistence
- **Java 8**: Stream API, Optional, LocalDateTime

## Features

### 1. Create Crypto Wallet
- Choose between Bitcoin (BTC) and Ethereum (ETH)
- Automatic valid address generation according to format
- Balance initialization
- Database persistence

### 2. Create Transaction
- Address validation (BTC/ETH formats)
- Fee level selection (ECONOMIQUE, STANDARD, RAPIDE)
- Automatic fee calculation based on crypto type
- Mempool addition with PENDING status

### 3. Mempool Position
- Queue position display
- Confirmation time estimation (Position × 10 min)
- Visual priority indicator

### 4. Fee Levels Comparison
- Elegant ASCII comparative table
- Calculated fees for each level
- Estimated mempool position
- Waiting time for each option

### 5. Mempool State
- Sorted pending transactions list by fees
- Anonymous transactions visualization
- Your transaction identification
- Statistics (min, max, average fees)

### 6. My Wallets
- List of all created wallets
- Balance for each wallet
- Current wallet identification

### 7. Global Statistics
- Wallet statistics (count, total balances)
- Transaction statistics (pending, confirmed)
- Mempool statistics (size, average fees)
- Database statistics

## Architecture

### Layered Structure

```
┌─────────────────────────────────────┐
│   Presentation Layer (UI/Menu)      │
│   app/ (8 handler classes)          │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│   Business Layer (Services)         │
│   service/ (6 service classes)      │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│   Data Layer (Repositories)         │
│   repository/ (4 classes + base)    │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│   PostgreSQL Database               │
│   Tables: wallets, transactions     │
└─────────────────────────────────────┘
```

### Design Patterns Implemented

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Singleton** | `DatabaseConnection`, `ApplicationContext` | Single instance management |
| **Repository** | `BaseRepository` + implementations | Data persistence abstraction |
| **Strategy** | `FeeCalculator` + implementations | Polymorphic fee calculations |
| **Template Method** | `Wallet` abstract class | Code reuse |
| **Factory** | `WalletService.createWallet()` | Type-based object creation |

### SOLID Principles

- **Single Responsibility**: Each class has one responsibility
- **Open/Closed**: Extensible without modification
- **Liskov Substitution**: BitcoinWallet/EthereumWallet interchangeable
- **Interface Segregation**: Minimal, targeted interfaces
- **Dependency Inversion**: Depends on abstractions

## Project Structure

```
crypto-wallet-simulator/
├── bin/                              # Compiled .class files (gitignored)
│   ├── app/                         # 8 application classes
│   ├── enums/                       # 3 enum classes
│   ├── exception/                   # 5 exception classes
│   ├── model/                       # 5 model classes
│   ├── repository/                  # 4 repository classes
│   ├── service/                     # 6 service classes
│   └── util/                        # 6 utility classes
├── lib/
│   └── postgresql-42.6.0.jar       # JDBC driver
├── sql/
│   └── schema.sql                   # Database schema
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/
│   │   │   │   ├── ApplicationContext.java
│   │   │   │   ├── InputReader.java
│   │   │   │   ├── Main.java
│   │   │   │   ├── MenuHandler.java
│   │   │   │   ├── WalletHandler.java
│   │   │   │   ├── TransactionHandler.java
│   │   │   │   ├── MempoolHandler.java
│   │   │   │   └── StatisticsHandler.java
│   │   │   ├── enums/
│   │   │   │   ├── CryptoType.java
│   │   │   │   ├── FeeLevel.java
│   │   │   │   └── TransactionStatus.java
│   │   │   ├── exception/
│   │   │   │   ├── InvalidAddressException.java
│   │   │   │   ├── InvalidAmountException.java
│   │   │   │   ├── TransactionException.java
│   │   │   │   └── WalletNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── BitcoinWallet.java
│   │   │   │   ├── EthereumWallet.java
│   │   │   │   ├── Mempool.java
│   │   │   │   ├── Transaction.java
│   │   │   │   └── Wallet.java
│   │   │   ├── repository/
│   │   │   │   ├── BaseRepository.java
│   │   │   │   ├── MempoolRepository.java
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   └── WalletRepository.java
│   │   │   ├── service/
│   │   │   │   ├── BitcoinFeeCalculator.java
│   │   │   │   ├── EthereumFeeCalculator.java
│   │   │   │   ├── FeeCalculator.java
│   │   │   │   ├── MempoolService.java
│   │   │   │   ├── TransactionService.java
│   │   │   │   └── WalletService.java
│   │   │   └── util/
│   │   │       ├── AddressValidator.java
│   │   │       ├── ConsolePrinter.java
│   │   │       ├── DatabaseConnection.java
│   │   │       ├── EnvLoader.java
│   │   │       ├── LoggerUtil.java
│   │   │       └── UUIDGenerator.java
│   │   └── resources/
│   └── test/
│       └── java/
├── .env                             # Database configuration
├── .gitignore                       # Git ignore rules
├── compile.sh                       # Compilation script
├── run.sh                          # Execution script
├── CryptoWallet.log                # Application logs
└── README.md
```

## Technologies Used

- **Java 8** - Main language
- **PostgreSQL 12+** - Relational database
- **JDBC 42.6.0** - PostgreSQL connection driver
- **Java Util Logging** - Logging system
- **Java Time API** - Date and duration management
- **Stream API** - Functional collection manipulation

## Prerequisites

- JDK 8 (exactly, no higher version)
- PostgreSQL 12+ installed and running
- PostgreSQL JDBC Driver (`postgresql-42.6.0.jar`)
- 100 MB disk space
- Terminal with UTF-8 support (for ASCII tables)

## Installation

### 1. Clone the project

```bash
git clone https://github.com/your-username/crypto-wallet-simulator.git
cd crypto-wallet-simulator
```

### 2. Configure PostgreSQL

```bash
# Create database
sudo -u postgres psql
CREATE DATABASE crypto_wallet_db;
CREATE USER your_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE crypto_wallet_db TO your_user;
\q

# Execute SQL script
psql -U your_user -d crypto_wallet_db -f sql/schema.sql
```

### 3. Configure environment

```bash
# Create .env file at project root
cat > .env << 'EOF'
DB_URL=jdbc:postgresql://localhost:5432/crypto_wallet_db
DB_USER=your_user
DB_PASSWORD=your_password
EOF
```

### 4. Download JDBC Driver

```bash
cd lib/
wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar
```

### 5. Compile the project

```bash
# Make scripts executable
chmod +x compile.sh run.sh

# Compile
./compile.sh
```

### 6. Launch application

```bash
./run.sh
```

## Usage Guide

### Create Wallet

1. Select option `1` from menu
2. Choose type (Bitcoin or Ethereum)
3. Unique address automatically generated
4. Optional: Initialize test balance

### Create Transaction

1. Ensure you have a created wallet
2. Select option `2`
3. Enter destination address (valid format required)
4. Enter amount to send
5. Choose fee level:
   - **ECONOMIQUE**: Slow (30-60 min), fees × 0.5
   - **STANDARD**: Medium (10-20 min), fees × 1.0
   - **RAPIDE**: Fast (1-5 min), fees × 2.0

### Compare Fees

1. Select option `4`
2. Choose crypto type
3. Enter amount
4. Comparative table displays with:
   - Fees for each level
   - Estimated mempool position
   - Estimated waiting time

## Screenshots

### Main Menu
```
════════════════════════════════════════════════════════════
            🪙 CRYPTO WALLET SIMULATOR 🪙
════════════════════════════════════════════════════════════

1️⃣  Create crypto wallet
2️⃣  Create new transaction
3️⃣  View position in mempool
4️⃣  Compare 3 fee levels
5️⃣  View mempool state
6️⃣  Display my wallets
7️⃣  Global statistics
0️⃣  Quit
────────────────────────────────────────────────────────────
👉 Your choice:
```

### Fee Comparison Table

```
┌───────────────┬──────────────────┬───────────────┬────────────────────┐
│ Level         │ Fees (BTC)       │ Position      │ Estimated time     │
├───────────────┼──────────────────┼───────────────┼────────────────────┤
│ ECONOMIQUE    │ 0.00002500       │ ~15           │ 150 min            │
│ STANDARD      │ 0.00005000       │ ~8            │ 80 min             │
│ RAPIDE        │ 0.00010000       │ ~3            │ 30 min             │
└───────────────┴──────────────────┴───────────────┴────────────────────┘
```

## Database Structure

### wallets table

| Column | Type | Description |
|---------|------|-------------|
| id | VARCHAR(36) | Unique UUID |
| address | VARCHAR(100) | Unique crypto address |
| crypto_type | VARCHAR(20) | BITCOIN or ETHEREUM |
| balance | DECIMAL(20,8) | Balance in crypto units |
| created_at | TIMESTAMP | Creation date |

### transactions table

| Column | Type | Description |
|---------|------|-------------|
| id | VARCHAR(36) | Unique UUID |
| from_address | VARCHAR(100) | Source address |
| to_address | VARCHAR(100) | Destination address |
| amount | DECIMAL(20,8) | Transferred amount |
| fees | DECIMAL(20,8) | Transaction fees |
| crypto_type | VARCHAR(20) | BITCOIN or ETHEREUM |
| fee_level | VARCHAR(20) | ECONOMIQUE, STANDARD, RAPIDE |
| status | VARCHAR(20) | PENDING, CONFIRMED, REJECTED |
| created_at | TIMESTAMP | Creation date |

## Testing

Run compilation tests:

```bash
./compile.sh
```

Run application:

```bash
./run.sh
```

## Logging

Logs are managed with `java.util.logging`:

- **System.out.println**: ONLY for UI/menu
- **LoggerUtil**: For all errors and critical operations

Log file: `CryptoWallet.log`

## Configuration

Modify connection parameters in `.env` file:

```properties
DB_URL=jdbc:postgresql://localhost:5432/crypto_wallet_db
DB_USER=your_username
DB_PASSWORD=your_password
```

## Troubleshooting

### Problem: "Driver not found"
- Check that `postgresql-42.6.0.jar` is in `lib/`
- Add it to CLASSPATH

### Problem: "Connection refused"
- Check PostgreSQL is running: `sudo systemctl status postgresql`
- Verify connection credentials in `.env`

### Problem: ASCII characters not displaying properly
- Configure terminal in UTF-8
- On Windows, use `chcp 65001`

### Problem: ".env not found"
- Ensure `.env` is at project root
- Check file permissions

## Project Management

- **Git**: Feature branch workflow
- **Commits**: Clear, descriptive messages following conventions
- **JIRA**: Task tracking and sprint planning

## Author

**Moustapha** - Java Developer  
📧 Email: moustapha@example.com  
🔗 GitHub: [your-profile](https://github.com/your-profile)

## License

This project is developed for educational purposes.

## Acknowledgments

- PostgreSQL Community
- Oracle Java Official Resources
- Open Source Contributors

---

⭐ **If this project helped you, please give it a star