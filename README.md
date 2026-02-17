# Game Transaction Report Application

## Overview

The Game Transaction Application is a **Spring MVC web application** for generating game transaction reports from MySQL databases. It provides comprehensive transaction search, filtering, sorting, and pagination capabilities with a modern, responsive web interface.

**Primary Function**: Query, analyze, and report on game transaction data with flexible filtering and sorting

---

## Quick Start

### What You Need to Know First

This application uses:
- **Java 8+** 
- **Spring Framework 5.3**
- **Spring Data JPA**
- **Hibernate 5.6**
- **MySQL 8.0**
- **Tomcat 9**

**Key Design**: Database-level pagination using Spring Data JPA's `Pageable` interface. All filtering, sorting, and pagination are handled on the server side.

---

## Prerequisites

Before you start, ensure you have:

### Required
- **Java 8 JDK** or higher
  ```bash
  java -version
  # Should show: openjdk version "1.8.0" or higher
  ```

- **Maven 3.8+**
  ```bash
  mvn -version
  # Should show: Apache Maven 3.8.1 or higher
  ```

- **MySQL 8.0+**
  ```bash
  mysql --version
  # Should show: mysql Ver 8.0.x
  ```

### Installation

**Windows:**
1. Java: Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use [OpenJDK](https://jdk.java.net/)
2. Maven: Download from [apache.org](https://maven.apache.org/download.cgi), extract, and add to PATH
3. MySQL: Download [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

**macOS (using Homebrew):**
```bash
brew install openjdk@8
brew install maven
brew install mysql
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install openjdk-8-jdk maven mysql-server
```

---

## Get the System Running

### Step 1: Clone the Repository

```bash
cd D:\Workspace\java
git clone <repository-url>
cd game-transaction-application
```

### Step 2: Setup the Database

#### Initialize from Script provided

```bash
# Start MySQL server
mysql -u root -p

# Create database and tables
mysql -u root -p < database-setup.sql

# Verify
mysql -u root -p -e "USE game_transactions; SELECT COUNT(*) FROM account_tran;"
```


### Step 3: Configure Database Connection

Edit `src/main/resources/database.properties`:

```properties
# Database connection
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/game_transactions?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=password

# Connection pool
db.pool.size=10
db.pool.maxSize=20
```

**Update `db.password` to your MySQL root password**

### Step 4: Build the Application

```bash
# Clean and build
mvn clean package

# Output: target/game-transaction-application.war (success!)
```

### Step 5: Deploy and Run

#### Apache Tomcat (Recommended for Development)

1. **Download Tomcat 9+** from [tomcat.apache.org](https://tomcat.apache.org/)
2. **Extract** to a directory (e.g., `C:\apache-tomcat-9.0.70`)
3. **Copy WAR file**:
   ```bash
   copy target\game-transaction-report.war C:\apache-tomcat-9.0.70\webapps\
   ```
4. **Start Tomcat**:
   - Windows: `C:\apache-tomcat-9.0.70\bin\startup.bat`
   - Linux/Mac: `./apache-tomcat-9.0.70/bin/startup.sh`
5. **Access**: http://localhost:8080/game-transaction-application


### Step 6: Verify Installation

Open your browser and navigate to:
```
http://localhost:8080/game-transaction-application
```

You should see the search form. Try:
1. **Enter date range**: Start date and end date
2. **Click**: "Generate Report"
3. **See**: Transaction data

---

## Architecture Overview

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Browser (User)                           │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTP
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Tomcat Application Server                    │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  Spring DispatcherServlet                │  │
│  │  (Routes HTTP requests to appropriate handlers)          │  │
│  └────┬─────────────────────────────────────────────────┬───┘  │
│       │                                                 │       │
│       ▼                                                 ▼       │
│  ┌──────────────────┐                         ┌──────────────┐ │
│  │  Controller      │                         │  View Layer  │ │
│  │  (HTTP handlers) │                         │  (JSP files) │ │
│  └────┬─────────────┘                         └──────────────┘ │
│       │                                                        │
│       ▼                                                        │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │              Service Layer (Business Logic)              │ │
│  │  - SearchCriteria building                              │ │
│  │  - PageRequest creation (pagination)                    │ │
│  │  - Sort object building                                 │ │
│  │  - Data validation                                      │ │
│  └────┬─────────────────────────────────────────────────┬──┘ │
│       │                                                 │     │
│       ▼                                                 │     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │        Repository Layer (Spring Data JPA)            │    │
│  │  - JpaRepository interface                           │    │
│  │  - PageRequest parameter handling                    │    │
│  │  - Dynamic query filtering                           │    │
│  └────┬─────────────────────────────────────────────────┘    │
│       │                                                       │
└───────┼───────────────────────────────────────────────────────┘
        │ HibernateSQL
        ▼
┌─────────────────────────────────────────────────────────────────┐
│                         MySQL Database                          │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              account_tran Table (100K+ rows)             │  │
│  │  - Indexed for fast queries                              │  │
│  │  - Pagination via LIMIT/OFFSET (Spring Data generates)   │  │
│  │  - Filtered WHERE clause                                 │  │
│  │  - Sorted ORDER BY                                       │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Data Flow Example

**User searches for transactions:**

```
1. User → Browser
   Input: Date range (2026-02-01 to 2026-02-16)
         Account ID (1005)
         Sort by: dateTime DESC
         Page: 0, Size: 25

2. Browser → HTTP POST /transaction/report
   Parameters: startDateTime, endDateTime, accountId, pageNumber, pageSize, sortBy, sortOrder

3. Controller (TransactionController.java)
   ├─ Validates input parameters
   ├─ Builds SearchCriteria with all filters
   └─ Calls transactionService.searchTransactions()

4. Service (AccountTranService.java)
   ├─ Creates Sort object: Sort.by(DESC, "dateTime")
   ├─ Creates PageRequest: PageRequest.of(0, 25, sort)
   └─ Calls repository.findWithAdvancedFilters(..., pageable)

5. Repository (AccountTranRepository.java)
   └─ Spring Data JPA builds SQL:
      SELECT * FROM account_tran
      WHERE dateTime >= ? AND dateTime <= ?
        AND accountId = ?
      ORDER BY dateTime DESC
      LIMIT 25 OFFSET 0;

6. MySQL Database
   ├─ Uses index: idx_account_datetime
   ├─ Filters rows with WHERE
   ├─ Orders by dateTime DESC
   ├─ Returns exactly 25 records
   └─ Also counts total matching records

7. Spring Data wraps results in Page<AccountTran>
   ├─ content: List of 25 transactions
   ├─ totalElements: 347 (total matching)
   ├─ totalPages: 14 (347 ÷ 25)
   └─ Other metadata

8. Controller → Model
   model.addAttribute("transactions", page.getContent());
   model.addAttribute("totalElements", page.getTotalElements());
   ... more attributes ...

9. View (report.jsp)
   ├─ Iterates over transactions
   ├─ Renders table rows
   ├─ Displays pagination controls

10. Browser → User
    HTML rendered with results
```

## HTTP API Reference

### Endpoints

#### GET /transaction/search
Display the search form

```
URL: http://localhost:8080/game-transaction-application/transaction/search
Method: GET
Response: HTML form with date/filter inputs
```

#### POST /transaction/report
Execute search and return results

```
URL: http://localhost:8080/game-transaction-application/transaction/report
Method: POST

Parameters:
  startDateTime: 2026-02-01T00:00 (required, ISO 8601 format)
  endDateTime: 2026-02-16T23:59 (required, ISO 8601 format)
  accountId: 1005 (optional, integer)
  platformTranId: PLAT (optional, string - partial match)
  gameTranId: GAME (optional, string - partial match)
  gameId: SLOTS (optional, string - partial match)
  tranType: WITHDRAWAL (optional, string - exact match)
  sortBy: dateTime (optional, default: id)
           Options: id, accountId, dateTime, tranType, 
                   platformTranId, gameTranId, gameId, amount, balance
  sortOrder: DESC (optional, default: DESC)
             Options: ASC, DESC
  pageNumber: 0 (optional, default: 0 - zero-based)
  pageSize: 25 (optional, default: 25)
            Max: 500

Response: HTML report page with:
  - Transaction table
  - Sortable column headers
  - Pagination controls
  - Search summary
  - Column filter
```

### Example Requests

```bash
# Search all transactions in date range
curl -X POST http://localhost:8080/game-transaction-application/transaction/report \
  -d "startDateTime=2026-02-01T00:00&endDateTime=2026-02-16T23:59"

# Search for specific account
curl -X POST http://localhost:8080/game-transaction-application/transaction/report \
  -d "startDateTime=2026-02-01T00:00&endDateTime=2026-02-16T23:59&accountId=1005"

# Search with sorting
curl -X POST http://localhost:8080/game-transaction-application/transaction/report \
  -d "startDateTime=2026-02-01T00:00&endDateTime=2026-02-16T23:59&sortBy=amount&sortOrder=DESC"

# Paginated search
curl -X POST http://localhost:8080/game-transaction-application/transaction/report \
  -d "startDateTime=2026-02-01T00:00&endDateTime=2026-02-16T23:59&pageNumber=5&pageSize=50"
```


---

## Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AccountTranServiceTest
```

---

## Troubleshooting

### Issue: "Cannot connect to database"

```bash
# Check MySQL is running
mysql -u root -p -e "SELECT 1;"

# Verify credentials in database.properties
# Ensure game_transactions database exists
mysql -u root -p -e "SHOW DATABASES;"
```


### Issue: "Build fails with 'cannot find symbol'"

```bash
# Clean cache and rebuild
mvn clean install -U -DskipTests

# Verify Java version
java -version  # Must be 8+

# Check Maven has dependencies
mvn dependency:tree
```

### Issue: "No transactions found"

```bash
# Verify database setup
mysql -u root -p game_transactions -e "SELECT COUNT(*) FROM account_tran;"

# Check sample data was loaded
mysql -u root -p game_transactions -e "SELECT * FROM account_tran LIMIT 5;"

# Try searching last 7 days (sample data is recent)
```

---

## 📝 Version Information

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 8+ | Language runtime |
| Spring Framework | 5.3.15 | MVC framework |
| Spring Data JPA | 2.x | ORM abstraction |
| Hibernate | 5.6.5 | ORM implementation |
| MySQL | 8.0+ | Database |
| Tomcat | 9+ | Application server |
| Maven | 3.8+ | Build tool |
| HikariCP | 4.0.3 | Connection pool |
