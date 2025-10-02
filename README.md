# 🪙 Crypto Wallet Simulator

Application console Java 8 simulant un portefeuille crypto avec mempool pour optimiser les frais de transaction.

![Java](https://img.shields.io/badge/Java-8-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue)
![JDBC](https://img.shields.io/badge/JDBC-42.6.0-green)
![Status](https://img.shields.io/badge/Status-Completed-success)

## 📋 Description

Ce projet simule le fonctionnement d'un wallet crypto avec un système de mempool (file d'attente de transactions). Les utilisateurs peuvent créer des wallets Bitcoin ou Ethereum, effectuer des transactions avec différents niveaux de frais, et visualiser leur position dans le mempool pour estimer le temps de confirmation.

### 🎯 Objectifs Pédagogiques

- **Architecture en couches** : Présentation, Métier, Données, Utilitaire
- **Design Patterns** : Singleton, Repository, Strategy, Template Method, Factory
- **Principes SOLID** : Respect strict des 5 principes
- **PostgreSQL/JDBC** : Persistence des données
- **Java 8** : Stream API, Optional, LocalDateTime

## ✨ Fonctionnalités

### 1️⃣ Créer un Wallet Crypto
- Choix entre Bitcoin (BTC) et Ethereum (ETH)
- Génération automatique d'adresse valide selon le format
- Initialisation du solde
- Sauvegarde en base de données

### 2️⃣ Créer une Transaction
- Validation des adresses (formats BTC/ETH)
- Sélection du niveau de frais (ECONOMIQUE, STANDARD, RAPIDE)
- Calcul automatique des frais selon le type de crypto
- Ajout au mempool avec statut PENDING

### 3️⃣ Position dans le Mempool
- Affichage de la position dans la file d'attente
- Estimation du temps de confirmation (Position × 10 min)
- Indicateur visuel de priorité

### 4️⃣ Comparaison des Niveaux de Frais
- Tableau comparatif ASCII élégant
- Frais calculés pour chaque niveau
- Position estimée dans le mempool
- Temps d'attente pour chaque option

### 5️⃣ État du Mempool
- Liste des transactions en attente triées par frais
- Visualisation des transactions anonymes
- Identification de votre transaction
- Statistiques (min, max, moyenne des frais)

### 6️⃣ Mes Wallets
- Liste de tous vos wallets créés
- Solde de chaque wallet
- Identification du wallet actuel

### 7️⃣ Statistiques Globales
- Statistiques des wallets (nombre, soldes totaux)
- Statistiques des transactions (pending, confirmées)
- Statistiques du mempool (taille, frais moyens)
- Statistiques de la base de données

## 🏛️ Architecture

### Structure en Couches

```
┌─────────────────────────────────────┐
│   Couche Présentation (UI/Menu)    │
│         app/Main.java               │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│       Couche Métier (Services)      │
│  WalletService, TransactionService  │
│  MempoolService, FeeCalculators     │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│    Couche Données (Repositories)    │
│  WalletRepository                   │
│  TransactionRepository              │
└────────────────┬────────────────────┘
                 │
┌────────────────┴────────────────────┐
│       PostgreSQL Database           │
│    Tables: wallets, transactions    │
└─────────────────────────────────────┘
```

### Design Patterns Implémentés

| Pattern | Implémentation | Objectif |
|---------|----------------|----------|
| **Singleton** | `DatabaseConnection` | Une seule instance de connexion DB |
| **Repository** | `WalletRepository`, `TransactionRepository` | Abstraction de la persistance |
| **Strategy** | `FeeCalculator` + implémentations | Calculs de frais polymorphes |
| **Template Method** | `Wallet` abstract | Réutilisation du code commun |
| **Factory** | `WalletService.createWallet()` | Création d'objets selon le type |

### Principes SOLID

✅ **S**ingle Responsibility : Chaque classe a une seule responsabilité  
✅ **O**pen/Closed : Extensible sans modification (interfaces, abstract)  
✅ **L**iskov Substitution : BitcoinWallet/EthereumWallet interchangeables  
✅ **I**nterface Segregation : Interfaces minimales et ciblées  
✅ **D**ependency Inversion : Dépendance aux abstractions  

## 🛠️ Technologies Utilisées

- **Java 8** - Langage principal
- **PostgreSQL 12+** - Base de données relationnelle
- **JDBC 42.6.0** - Driver de connexion PostgreSQL
- **Java Util Logging** - Système de logging
- **Java Time API** - Gestion des dates et durées
- **Stream API** - Manipulation fonctionnelle des collections

## 📦 Prérequis

- JDK 8 (exactement, pas de version supérieure)
- PostgreSQL 12+ installé et démarré
- Driver JDBC PostgreSQL (`postgresql-42.6.0.jar`)
- 100 MB d'espace disque
- Terminal avec support UTF-8 (pour les tableaux ASCII)

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/votre-username/crypto-wallet-simulator.git
cd crypto-wallet-simulator
```

### 2. Configurer PostgreSQL

```bash
# Créer la base de données
sudo -u postgres psql
CREATE DATABASE crypto_wallet_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE crypto_wallet_db TO postgres;
\q

# Exécuter le script SQL
psql -U postgres -d crypto_wallet_db -f sql/schema.sql
```

### 3. Télécharger le Driver JDBC

```bash
cd lib/
wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar
```

### 4. Compiler le projet

```bash
cd src/main/java
javac -cp ".:../../../lib/postgresql-42.6.0.jar" \
  enums/*.java exception/*.java util/*.java \
  model/*.java service/*.java repository/*.java \
  app/Main.java
```

### 5. Lancer l'application

```bash
java -cp ".:../../../lib/postgresql-42.6.0.jar" app.Main
```

## 📖 Guide d'Utilisation

### Créer un Wallet

1. Sélectionnez l'option `1` dans le menu
2. Choisissez le type (Bitcoin ou Ethereum)
3. Une adresse unique est générée automatiquement
4. Optionnel : Initialisez un solde de test

### Créer une Transaction

1. Assurez-vous d'avoir un wallet créé
2. Sélectionnez l'option `2`
3. Entrez l'adresse de destination (format valide requis)
4. Entrez le montant à envoyer
5. Choisissez le niveau de frais :
   - **ECONOMIQUE** : Lent (30-60 min), frais × 0.5
   - **STANDARD** : Moyen (10-20 min), frais × 1.0
   - **RAPIDE** : Rapide (1-5 min), frais × 2.0

### Comparer les Frais

1. Sélectionnez l'option `4`
2. Choisissez le type de crypto
3. Entrez le montant
4. Un tableau comparatif s'affiche avec :
   - Frais pour chaque niveau
   - Position estimée dans le mempool
   - Temps d'attente estimé

## 📊 Captures d'Écran

```
═══════════════════════════════════════════════════════════
            🪙 CRYPTO WALLET SIMULATOR 🪙
═══════════════════════════════════════════════════════════

1️⃣  Créer un wallet crypto
2️⃣  Créer une nouvelle transaction
3️⃣  Voir ma position dans le mempool
4️⃣  Comparer les 3 niveaux de frais
5️⃣  Consulter l'état du mempool
6️⃣  Afficher mes wallets
7️⃣  Statistiques globales
0️⃣  Quitter
────────────────────────────────────────────────────────────
👉 Votre choix :
```

### Tableau de Comparaison des Frais

```
┌───────────────┬──────────────────┬───────────────┬────────────────────┐
│ Niveau        │ Frais (BTC)      │ Position      │ Temps estimé       │
├───────────────┼──────────────────┼───────────────┼────────────────────┤
│ ECONOMIQUE    │ 0.00002500       │ ~15           │ 150 min            │
│ STANDARD      │ 0.00005000       │ ~8            │ 80 min             │
│ RAPIDE        │ 0.00010000       │ ~3            │ 30 min             │
└───────────────┴──────────────────┴───────────────┴────────────────────┘
```

## 🗄️ Structure de la Base de Données

### Table : wallets

| Colonne | Type | Description |
|---------|------|-------------|
| id | VARCHAR(36) | UUID unique |
| address | VARCHAR(100) | Adresse crypto unique |
| crypto_type | VARCHAR(20) | BITCOIN ou ETHEREUM |
| balance | DECIMAL(20,8) | Solde en unités crypto |
| created_at | TIMESTAMP | Date de création |

### Table : transactions

| Colonne | Type | Description |
|---------|------|-------------|
| id | VARCHAR(36) | UUID unique |
| from_address | VARCHAR(100) | Adresse source |
| to_address | VARCHAR(100) | Adresse destination |
| amount | DECIMAL(20,8) | Montant transféré |
| fees | DECIMAL(20,8) | Frais de transaction |
| crypto_type | VARCHAR(20) | BITCOIN ou ETHEREUM |
| fee_level | VARCHAR(20) | ECONOMIQUE, STANDARD, RAPIDE |
| status | VARCHAR(20) | PENDING, CONFIRMED, REJECTED |
| created_at | TIMESTAMP | Date de création |

## 🧪 Tests

Le projet inclut plusieurs classes de test :

```bash
# Test de connexion DB
java -cp ".:../../../lib/postgresql-42.6.0.jar" test.DatabaseTest

# Test des repositories
java -cp ".:../../../lib/postgresql-42.6.0.jar" test.RepositoryTest

# Test des services
java -cp ".:../../../lib/postgresql-42.6.0.jar" test.ServicesTest
```

## 📝 Logs

Les logs sont gérés avec `java.util.logging` :

- **System.out.println** : UNIQUEMENT pour l'UI/menu
- **LoggerUtil** : Pour toutes les erreurs et opérations critiques

Exemple de logs :
```
INFO: Wallet créé: 8a3f2b1c [BITCOIN] - Adresse: 1A1zP1eP5QG...
INFO: TRANSACTION [9b2a1f3d] - CREATED: Fees: 0.00005000 BTC
SEVERE: Erreur lors de la sauvegarde : Connection refused
```

## 🔧 Configuration

Modifier les paramètres de connexion dans `util/DatabaseConnection.java` :

```java
private static final String URL = "jdbc:postgresql://localhost:5432/crypto_wallet_db";
private static final String USER = "postgres";
private static final String PASSWORD = "votre_mot_de_passe";
```

## 🐛 Dépannage

### Problème : "Driver not found"
- Vérifiez que `postgresql-42.6.0.jar` est dans `lib/`
- Ajoutez-le au CLASSPATH

### Problème : "Connection refused"
- Vérifiez que PostgreSQL est démarré : `sudo systemctl status postgresql`
- Vérifiez les identifiants de connexion

### Problème : Caractères ASCII mal affichés
- Configurez votre terminal en UTF-8
- Sur Windows, utilisez `chcp 65001`

## 👥 Auteur

**Moustapha** - Développeur Java  
📧 Email: votre.email@example.com  
🔗 LinkedIn: [Votre profil](https://linkedin.com/in/votre-profil)

## 📄 Licence

Ce projet est développé dans un cadre pédagogique.

## 🙏 Remerciements

- Anthropic (Claude) pour l'inspiration
- Communauté PostgreSQL
- Ressources Java officielles d'Oracle

---

⭐ **Si ce projet vous a aidé, n'hésitez pas à lui donner une étoile !**