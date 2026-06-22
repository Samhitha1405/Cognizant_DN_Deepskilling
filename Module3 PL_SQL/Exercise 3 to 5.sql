-- Exercise -3 Stored Procedures
-- Scenario 1 - The bank needs to process monthly interest for all savings accounts
-- Process Monthly Interest
USE BankDB;

-- Disable Safe Update Mode for this session
SET SQL_SAFE_UPDATES = 0;

-- Drop procedure if it already exists
DROP PROCEDURE IF EXISTS ProcessMonthlyInterest;

DELIMITER $$

CREATE PROCEDURE ProcessMonthlyInterest()
BEGIN
    UPDATE Accounts
    SET Balance = Balance * 1.01,
        LastModified = CURDATE()
    WHERE AccountType = 'Savings';

    SELECT CONCAT(
        'Interest applied to ',
        ROW_COUNT(),
        ' savings account(s).'
    ) AS Status;
END$$

DELIMITER ;

-- Execute Procedure
CALL ProcessMonthlyInterest();

-- View Updated Accounts
SELECT
    AccountID,
    CustomerID,
    AccountType,
    Balance,
    LastModified
FROM Accounts;

-- Enable Safe Update Mode again (optional)
SET SQL_SAFE_UPDATES = 1;
-- Scenario 2: The bank wants to implement a bonus scheme for employees based on their performance.
-- Update Employee Bonus
USE BankDB;

SET SQL_SAFE_UPDATES = 0;

DROP PROCEDURE IF EXISTS UpdateEmployeeBonus;

DELIMITER $$

CREATE PROCEDURE UpdateEmployeeBonus(
    IN dept VARCHAR(50),
    IN bonusPct DECIMAL(5,2)
)
BEGIN
    DECLARE empCount INT;

    SELECT COUNT(*)
    INTO empCount
    FROM Employees
    WHERE Department = dept;

    IF empCount = 0 THEN
        SELECT CONCAT(
            'No employees found in department: ',
            dept
        ) AS Status;
    ELSE

        UPDATE Employees
        SET Salary = Salary + (Salary * bonusPct / 100)
        WHERE Department = dept;

        SELECT CONCAT(
            'Bonus of ',
            bonusPct,
            '% applied to ',
            empCount,
            ' employee(s) in ',
            dept
        ) AS Status;

    END IF;
END$$

DELIMITER ;

CALL UpdateEmployeeBonus('IT',15);
CALL UpdateEmployeeBonus('HR',10);
CALL UpdateEmployeeBonus('Finance',20);

SELECT * FROM Employees;

SET SQL_SAFE_UPDATES = 1;
-- Scenario 3: Customers should be able to transfer funds between their accounts.
-- Transfer Funds
USE BankDB;

-- Remove old procedure if it exists
DROP PROCEDURE IF EXISTS TransferFunds;

DELIMITER $$

CREATE PROCEDURE TransferFunds(
    IN fromAcc INT,
    IN toAcc INT,
    IN amount DECIMAL(10,2)
)
BEGIN
    DECLARE srcBalance DECIMAL(10,2);
    DECLARE srcExists INT;
    DECLARE tgtExists INT;

    -- Error handler
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Transfer Failed Due To Database Error' AS Status;
    END;

    -- Check source account exists
    SELECT COUNT(*)
    INTO srcExists
    FROM Accounts
    WHERE AccountID = fromAcc;

    -- Check target account exists
    SELECT COUNT(*)
    INTO tgtExists
    FROM Accounts
    WHERE AccountID = toAcc;

    IF srcExists = 0 THEN

        SELECT CONCAT(
            'ERROR: Source Account ',
            fromAcc,
            ' Not Found'
        ) AS Status;

    ELSEIF tgtExists = 0 THEN

        SELECT CONCAT(
            'ERROR: Target Account ',
            toAcc,
            ' Not Found'
        ) AS Status;

    ELSEIF amount <= 0 THEN

        SELECT 'ERROR: Transfer Amount Must Be Greater Than Zero'
        AS Status;

    ELSE

        SELECT Balance
        INTO srcBalance
        FROM Accounts
        WHERE AccountID = fromAcc;

        IF srcBalance < amount THEN

            SELECT CONCAT(
                'ERROR: Insufficient Balance. Available = ',
                srcBalance
            ) AS Status;

        ELSE

            START TRANSACTION;

            UPDATE Accounts
            SET Balance = Balance - amount
            WHERE AccountID = fromAcc;

            UPDATE Accounts
            SET Balance = Balance + amount
            WHERE AccountID = toAcc;

            COMMIT;

            SELECT CONCAT(
                'SUCCESS: ',
                amount,
                ' Transferred From Account ',
                fromAcc,
                ' To Account ',
                toAcc
            ) AS Status;

        END IF;

    END IF;

END$$

DELIMITER ;

-- TEST CASES
-- Valid Transfer
CALL TransferFunds(2,1,500);

-- Insufficient Balance
CALL TransferFunds(1,2,99999);

-- Invalid Amount
CALL TransferFunds(1,2,-100);

-- Invalid Source Account
CALL TransferFunds(99,1,100);

-- Invalid Target Account
CALL TransferFunds(1,99,100);

-- View Updated Balances
SELECT
    AccountID,
    CustomerID,
    AccountType,
    Balance
FROM Accounts;
-- Exercise 4 : Functions
SET GLOBAL log_bin_trust_function_creators = 1;
-- Scenario 1: Calculate the age of customers for eligibility checks.
-- Calculate Age
DELIMITER $$
CREATE FUNCTION CalculateAge(dob DATE)
RETURNS INT DETERMINISTIC
BEGIN
    RETURN TIMESTAMPDIFF(YEAR, dob, CURDATE());
END$$
DELIMITER ;

SELECT Name, DOB, CalculateAge(DOB) AS Age FROM Customers;
-- Scenario 2: The bank needs to compute the monthly installment for a loan.
-- Calculate Monthly Installment
DELIMITER $$
CREATE FUNCTION CalculateMonthlyInstallment(
    principal DECIMAL(10,2),
    annualRate DECIMAL(5,2),
    years INT
)
RETURNS DECIMAL(10,2) DETERMINISTIC
BEGIN
    DECLARE monthlyRate DECIMAL(10,6);
    DECLARE months INT;
    DECLARE emi DECIMAL(10,2);

    SET monthlyRate = annualRate / (12 * 100);
    SET months = years * 12;

    IF monthlyRate = 0 THEN
        SET emi = principal / months;
    ELSE
        SET emi = principal * monthlyRate * POW(1 + monthlyRate, months)
                  / (POW(1 + monthlyRate, months) - 1);
    END IF;

    RETURN ROUND(emi, 2);
END$$
DELIMITER ;

-- Test: 5000 loan, 5% annual, 5 years
SELECT CalculateMonthlyInstallment(5000, 5, 5) AS MonthlyEMI;
-- Scenario 3: Check if a customer has sufficient balance before making a transaction.
-- Has Sufficient Balance
DELIMITER $$
CREATE FUNCTION HasSufficientBalance(
    accID INT,
    requiredAmount DECIMAL(10,2)
)
RETURNS BOOLEAN DETERMINISTIC
BEGIN
    DECLARE bal DECIMAL(10,2);
    SELECT Balance INTO bal FROM Accounts WHERE AccountID = accID;
    RETURN bal >= requiredAmount;
END$$
DELIMITER ;

SELECT HasSufficientBalance(1, 500) AS CanWithdraw500;
SELECT HasSufficientBalance(1, 99999) AS CanWithdraw99999;
-- Exercise 5: Triggers
-- Scenario 1: Automatically update the last modified date when a customer's record is updated.
-- Update Customer Last Modified
DELIMITER $$
CREATE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    SET NEW.LastModified = CURDATE();
END$$
DELIMITER ;

-- Test
UPDATE Customers SET Balance = 2000 WHERE CustomerID = 1;
SELECT * FROM Customers;
-- Scenario 2: Maintain an audit log for all transactions.
-- Log Transaction
DELIMITER $$
CREATE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog(TransactionID, AccountID, Amount, TransactionType, LogDate)
    VALUES (NEW.TransactionID, NEW.AccountID, NEW.Amount, NEW.TransactionType, NOW());
END$$
DELIMITER ;

-- Test
INSERT INTO Transactions VALUES (3, 1, CURDATE(), 100, 'Deposit');
SELECT * FROM AuditLog;
-- Scenario 3: Enforce business rules on deposits and withdrawals.
-- Check Transaction Rules
USE BankDB;
-- Remove existing trigger if it exists
DROP TRIGGER IF EXISTS CheckTransactionRules;
DELIMITER $$
CREATE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
BEGIN
    DECLARE accBalance DECIMAL(10,2);
    -- Fetch current account balance
    SELECT Balance
    INTO accBalance
    FROM Accounts
    WHERE AccountID = NEW.AccountID;
    -- Rule 1: Withdrawal cannot exceed balance
    IF NEW.TransactionType = 'Withdrawal'
       AND NEW.Amount > accBalance THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'ERROR: Withdrawal exceeds account balance.';

    END IF;
    -- Rule 2: Deposit amount must be positive
    IF NEW.TransactionType = 'Deposit'
       AND NEW.Amount <= 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'ERROR: Deposit amount must be positive.';

    END IF;

END$$
DELIMITER ;
-- Check existing transactions first
SELECT * FROM Transactions;
-- TEST CASE 1 : Valid Deposit
INSERT INTO Transactions
VALUES
(
    10,
    1,
    CURDATE(),
    500,
    'Deposit'
);

-- TEST CASE 2 : Valid Withdrawal

INSERT INTO Transactions
VALUES
(
    11,
    1,
    CURDATE(),
    100,
    'Withdrawal'
);
-- TEST CASE 3 : Invalid Withdrawal
INSERT INTO Transactions
VALUES
(
    12,
    1,
    CURDATE(),
    99999,
    'Withdrawal'
);

-- TEST CASE 4 : Invalid Deposit

INSERT INTO Transactions
VALUES
(
    13,
    1,
    CURDATE(),
    -100,
    'Deposit'
);
-- View Final Transactions
SELECT
    TransactionID,
    AccountID,
    TransactionDate,
    Amount,
    TransactionType
FROM Transactions;