CREATE DATABASE IF NOT EXISTS BankDB;
USE BankDB;
-- Always set this before procedures/triggers
SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$

CREATE PROCEDURE MyProc()
BEGIN
    SELECT 'Hello';
END$$

DELIMITER ;
CREATE DATABASE IF NOT EXISTS BankDB;
USE BankDB;

CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY,
    Name VARCHAR(100),
    DOB DATE,
    Balance DECIMAL(10,2),
    LastModified DATE,
    IsVIP BOOLEAN DEFAULT FALSE
);

CREATE TABLE Accounts (
    AccountID INT PRIMARY KEY,
    CustomerID INT,
    AccountType VARCHAR(20),
    Balance DECIMAL(10,2),
    LastModified DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY,
    AccountID INT,
    TransactionDate DATE,
    Amount DECIMAL(10,2),
    TransactionType VARCHAR(10),
    FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID)
);

CREATE TABLE Loans (
    LoanID INT PRIMARY KEY,
    CustomerID INT,
    LoanAmount DECIMAL(10,2),
    InterestRate DECIMAL(5,2),
    StartDate DATE,
    EndDate DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(100),
    Position VARCHAR(50),
    Salary DECIMAL(10,2),
    Department VARCHAR(50),
    HireDate DATE
);

CREATE TABLE AuditLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    TransactionID INT,
    AccountID INT,
    Amount DECIMAL(10,2),
    TransactionType VARCHAR(10),
    LogDate DATETIME
);

CREATE TABLE ErrorLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    ErrorMessage VARCHAR(500),
    LogDate DATETIME DEFAULT NOW()
);

USE BankDB;

INSERT INTO Customers VALUES (1, 'John Doe',   '1955-05-15', 1000,  CURDATE(), FALSE);
INSERT INTO Customers VALUES (2, 'Jane Smith',  '1990-07-20', 15000, CURDATE(), FALSE);
INSERT INTO Customers VALUES (3, 'Bob Senior',  '1950-01-10', 500,   CURDATE(), FALSE);
INSERT INTO Customers VALUES (4, 'Alice Rich',  '1980-03-22', 12000, CURDATE(), FALSE);

INSERT INTO Accounts VALUES (1, 1, 'Savings',  1000,  CURDATE());
INSERT INTO Accounts VALUES (2, 2, 'Checking', 15000, CURDATE());
INSERT INTO Accounts VALUES (3, 3, 'Savings',  500,   CURDATE());
INSERT INTO Accounts VALUES (4, 4, 'Savings',  12000, CURDATE());

INSERT INTO Transactions VALUES (1, 1, CURDATE(), 200, 'Deposit');
INSERT INTO Transactions VALUES (2, 2, CURDATE(), 300, 'Withdrawal');

INSERT INTO Loans VALUES (1, 1, 5000, 5.0,  CURDATE(), DATE_ADD(CURDATE(), INTERVAL 60 MONTH));
INSERT INTO Loans VALUES (2, 3, 3000, 7.0,  CURDATE(), DATE_ADD(CURDATE(), INTERVAL 25 DAY));
INSERT INTO Loans VALUES (3, 4, 8000, 6.5,  CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY));

INSERT INTO Employees VALUES (1, 'Alice Johnson', 'Manager',   70000, 'HR', '2015-06-15');
INSERT INTO Employees VALUES (2, 'Bob Brown',     'Developer', 60000, 'IT', '2017-03-20');

-- Verifiying whether the data is inserteed or not

SELECT * FROM Customers;
SELECT * FROM Accounts;
SELECT * FROM Transactions;
SELECT * FROM Loans;
SELECT * FROM Employees;

-- To ensure that stored procedures/triggers run

SET GLOBAL log_bin_trust_function_creators = 1;
DELIMITER $$
-- Exercise 1 - Senior Citizen Loan Discount greater than 60
DELIMITER $$
CREATE PROCEDURE ApplyAgeDiscount()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE cID INT;
    DECLARE dob DATE;
    DECLARE age INT;

    DECLARE cur CURSOR FOR SELECT CustomerID, DOB FROM Customers;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO cID, dob;
        IF done THEN LEAVE read_loop; END IF;

        SET age = TIMESTAMPDIFF(YEAR, dob, CURDATE());

        IF age > 60 THEN
            UPDATE Loans
            SET InterestRate = InterestRate - 1
            WHERE CustomerID = cID AND InterestRate > 1;

            SELECT CONCAT('Discount applied for CustomerID: ', cID, ' | Age: ', age) AS Message;
        END IF;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL ApplyAgeDiscount();
-- Scenario 2 - Set Is VIP flag for balance > $10,000
DELIMITER $$
CREATE PROCEDURE SetVIPStatus()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE cID INT;
    DECLARE bal DECIMAL(10,2);

    DECLARE cur CURSOR FOR SELECT CustomerID, Balance FROM Customers;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    vip_loop: LOOP
        FETCH cur INTO cID, bal;
        IF done THEN LEAVE vip_loop; END IF;

        IF bal > 10000 THEN
            UPDATE Customers SET IsVIP = TRUE WHERE CustomerID = cID;
            SELECT CONCAT('CustomerID ', cID, ' promoted to VIP') AS Message;
        ELSE
            UPDATE Customers SET IsVIP = FALSE WHERE CustomerID = cID;
        END IF;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL SetVIPStatus();
SELECT * FROM Customers;
-- Scenario 3 - Reminders for loans due within 30 days
DELIMITER $$
CREATE PROCEDURE SendLoanReminders()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE lID INT;
    DECLARE cID INT;
    DECLARE endDt DATE;
    DECLARE cName VARCHAR(100);

    DECLARE cur CURSOR FOR
        SELECT l.LoanID, l.CustomerID, l.EndDate, c.Name
        FROM Loans l
        JOIN Customers c ON l.CustomerID = c.CustomerID
        WHERE l.EndDate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY);

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    remind_loop: LOOP
        FETCH cur INTO lID, cID, endDt, cName;
        IF done THEN LEAVE remind_loop; END IF;

        SELECT CONCAT(
            'REMINDER: Dear ', cName,
            ', your Loan ID ', lID,
            ' is due on ', endDt,
            '. Please ensure timely payment.'
        ) AS ReminderMessage;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL SendLoanReminders();
-- Exercise 2 - Error Handling
-- Handle exceptions during fund transfer
-- Safe Transfer Funds
DELIMITER $$
CREATE PROCEDURE SafeTransferFunds(
    IN fromAcc INT,
    IN toAcc INT,
    IN amount DECIMAL(10,2)
)
BEGIN
    DECLARE currentBalance DECIMAL(10,2);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        INSERT INTO ErrorLog(ErrorMessage) 
        VALUES (CONCAT('Transfer failed between Account ', fromAcc, ' and ', toAcc));
        SELECT 'ERROR: Transaction rolled back due to an unexpected error.' AS Status;
    END;

    START TRANSACTION;

    SELECT Balance INTO currentBalance FROM Accounts WHERE AccountID = fromAcc FOR UPDATE;

    IF currentBalance < amount THEN
        ROLLBACK;
        INSERT INTO ErrorLog(ErrorMessage)
        VALUES (CONCAT('Insufficient funds in Account ', fromAcc));
        SELECT 'ERROR: Insufficient funds. Transfer cancelled.' AS Status;
    ELSE
        UPDATE Accounts SET Balance = Balance - amount WHERE AccountID = fromAcc;
        UPDATE Accounts SET Balance = Balance + amount WHERE AccountID = toAcc;
        COMMIT;
        SELECT 'SUCCESS: Funds transferred successfully.' AS Status;
    END IF;
END$$
DELIMITER ;

-- Sample Testing Data
CALL SafeTransferFunds(1, 2, 200);   -- Should succeed
CALL SafeTransferFunds(1, 2, 99999); -- Insufficient Funds
SELECT * FROM Accounts;
SELECT * FROM ErrorLog;
-- Scenario 2 - Managing errors when updating employee salary
-- Update Salary
DELIMITER $$
CREATE PROCEDURE UpdateSalary(
    IN empID INT,
    IN pct DECIMAL(5,2)
)
BEGIN
    DECLARE empCount INT;

    SELECT COUNT(*) INTO empCount FROM Employees WHERE EmployeeID = empID;

    IF empCount = 0 THEN
        INSERT INTO ErrorLog(ErrorMessage)
        VALUES (CONCAT('Employee ID ', empID, ' not found.'));
        SELECT CONCAT('ERROR: Employee ID ', empID, ' does not exist.') AS Status;
    ELSE
        UPDATE Employees
        SET Salary = Salary + (Salary * pct / 100)
        WHERE EmployeeID = empID;
        SELECT CONCAT('SUCCESS: Salary updated for Employee ID ', empID) AS Status;
    END IF;
END$$
DELIMITER ;
-- Testing Sample
CALL UpdateSalary(1, 10);   -- 10% raise for Employee 1
CALL UpdateSalary(99, 10);  -- Non-existent employee
SELECT * FROM Employees;
SELECT * FROM ErrorLog;
-- Scenario 3 - Ensure Data Integrity when adding a new customer
-- Add New Customer
DELIMITER $$
CREATE PROCEDURE AddNewCustomer(
    IN cID INT,
    IN cName VARCHAR(100),
    IN cDOB DATE,
    IN cBalance DECIMAL(10,2)
)
BEGIN
    DECLARE dupCount INT;

    SELECT COUNT(*) INTO dupCount FROM Customers WHERE CustomerID = cID;

    IF dupCount > 0 THEN
        INSERT INTO ErrorLog(ErrorMessage)
        VALUES (CONCAT('Duplicate CustomerID: ', cID, ' — insertion prevented.'));
        SELECT CONCAT('ERROR: Customer ID ', cID, ' already exists.') AS Status;
    ELSE
        INSERT INTO Customers(CustomerID, Name, DOB, Balance, LastModified)
        VALUES (cID, cName, cDOB, cBalance, CURDATE());
        SELECT CONCAT('SUCCESS: Customer ', cName, ' added.') AS Status;
    END IF;
END$$
DELIMITER ;
-- Testing Sample
CALL AddNewCustomer(5, 'Charlie New', '2000-01-01', 2000);  -- New customer
CALL AddNewCustomer(1, 'Duplicate',   '2000-01-01', 500);   -- Duplicate
SELECT * FROM Customers;
