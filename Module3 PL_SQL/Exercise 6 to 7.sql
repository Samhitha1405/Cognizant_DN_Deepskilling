-- Exercise 6 : Cursors
-- Scenario 1: Generate monthly statements for all customers.
-- Generate Monthly Statements
DELIMITER $$
CREATE PROCEDURE GenerateMonthlyStatements()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE tID INT;
    DECLARE aID INT;
    DECLARE tDate DATE;
    DECLARE amt DECIMAL(10,2);
    DECLARE tType VARCHAR(10);

    DECLARE cur CURSOR FOR
        SELECT TransactionID, AccountID, TransactionDate, Amount, TransactionType
        FROM Transactions
        WHERE MONTH(TransactionDate) = MONTH(CURDATE())
          AND YEAR(TransactionDate)  = YEAR(CURDATE());

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    stmt_loop: LOOP
        FETCH cur INTO tID, aID, tDate, amt, tType;
        IF done THEN LEAVE stmt_loop; END IF;

        SELECT CONCAT(
            'Statement | TxnID: ', tID,
            ' | AccountID: ', aID,
            ' | Date: ', tDate,
            ' | Type: ', tType,
            ' | Amount: $', amt
        ) AS MonthlyStatement;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL GenerateMonthlyStatements();
-- Scenario 2: Apply annual fee to all accounts.
-- Apply AnnualFee
DELIMITER $$
CREATE PROCEDURE ApplyAnnualFee()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE aID INT;
    DECLARE bal DECIMAL(10,2);
    DECLARE fee DECIMAL(10,2) DEFAULT 50.00;

    DECLARE cur CURSOR FOR SELECT AccountID, Balance FROM Accounts;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    fee_loop: LOOP
        FETCH cur INTO aID, bal;
        IF done THEN LEAVE fee_loop; END IF;

        UPDATE Accounts
        SET Balance = Balance - fee, LastModified = CURDATE()
        WHERE AccountID = aID;

        SELECT CONCAT('Annual fee deducted from AccountID: ', aID,
                      ' | Old Balance: ', bal,
                      ' | New Balance: ', bal - fee) AS FeeMessage;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL ApplyAnnualFee();
SELECT * FROM Accounts;
-- Scenario 3: Update the interest rate for all loans based on a new policy.
-- Update Loan Interest Rates
DELIMITER $$
CREATE PROCEDURE UpdateLoanInterestRates()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE lID INT;
    DECLARE oldRate DECIMAL(5,2);
    DECLARE newRate DECIMAL(5,2);

    DECLARE cur CURSOR FOR SELECT LoanID, InterestRate FROM Loans;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    rate_loop: LOOP
        FETCH cur INTO lID, oldRate;
        IF done THEN LEAVE rate_loop; END IF;

        -- Policy: reduce by 0.5% if above 6%, else increase by 0.25%
        IF oldRate > 6 THEN
            SET newRate = oldRate - 0.5;
        ELSE
            SET newRate = oldRate + 0.25;
        END IF;

        UPDATE Loans SET InterestRate = newRate WHERE LoanID = lID;

        SELECT CONCAT('LoanID: ', lID,
                      ' | Old Rate: ', oldRate,
                      '% | New Rate: ', newRate, '%') AS RateUpdate;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;

CALL UpdateLoanInterestRates();
SELECT * FROM Loans;
-- Exercise 7: Packages
-- Scenario 1: Group all customer-related procedures and functions into a package.
-- Customer Management Package
-- Add new customer
DELIMITER $$
CREATE PROCEDURE CustomerManagement_AddCustomer(
    IN cID INT, IN cName VARCHAR(100), IN cDOB DATE, IN cBal DECIMAL(10,2)
)
BEGIN
    INSERT INTO Customers(CustomerID, Name, DOB, Balance, LastModified)
    VALUES (cID, cName, cDOB, cBal, CURDATE());
    SELECT 'Customer added successfully.' AS Status;
END$$

-- Update customer details
CREATE PROCEDURE CustomerManagement_UpdateCustomer(
    IN cID INT, IN cName VARCHAR(100), IN cBal DECIMAL(10,2)
)
BEGIN
    UPDATE Customers SET Name = cName, Balance = cBal WHERE CustomerID = cID;
    SELECT 'Customer updated.' AS Status;
END$$

-- Get customer balance
CREATE FUNCTION CustomerManagement_GetBalance(cID INT)
RETURNS DECIMAL(10,2) DETERMINISTIC
BEGIN
    DECLARE bal DECIMAL(10,2);
    SELECT Balance INTO bal FROM Customers WHERE CustomerID = cID;
    RETURN bal;
END$$
DELIMITER ;

-- Test
CALL CustomerManagement_AddCustomer(6, 'Eve New', '1995-08-08', 3000);
CALL CustomerManagement_UpdateCustomer(6, 'Eve Updated', 3500);
SELECT CustomerManagement_GetBalance(6) AS Balance;
-- Scenario 2: Create a package to manage employee data.
-- Employee Management Package
DELIMITER $$
CREATE PROCEDURE EmployeeManagement_HireEmployee(
    IN eID INT, IN eName VARCHAR(100), IN pos VARCHAR(50),
    IN sal DECIMAL(10,2), IN dept VARCHAR(50), IN hDate DATE
)
BEGIN
    INSERT INTO Employees VALUES (eID, eName, pos, sal, dept, hDate);
    SELECT 'Employee hired.' AS Status;
END$$

CREATE PROCEDURE EmployeeManagement_UpdateEmployee(
    IN eID INT, IN pos VARCHAR(50), IN dept VARCHAR(50)
)
BEGIN
    UPDATE Employees SET Position = pos, Department = dept WHERE EmployeeID = eID;
    SELECT 'Employee updated.' AS Status;
END$$

CREATE FUNCTION EmployeeManagement_AnnualSalary(eID INT)
RETURNS DECIMAL(10,2) DETERMINISTIC
BEGIN
    DECLARE monthlySal DECIMAL(10,2);
    SELECT Salary INTO monthlySal FROM Employees WHERE EmployeeID = eID;
    RETURN monthlySal * 12;
END$$
DELIMITER ;

CALL EmployeeManagement_HireEmployee(3, 'Carol White', 'Analyst', 50000, 'Finance', '2024-01-10');
SELECT EmployeeManagement_AnnualSalary(1) AS AnnualSalary;
-- Scenario 3: Group all account-related operations into a package.
-- Account Operations Package
DELIMITER $$
CREATE PROCEDURE AccountOperations_OpenAccount(
    IN aID INT, IN cID INT, IN aType VARCHAR(20), IN initBal DECIMAL(10,2)
)
BEGIN
    INSERT INTO Accounts(AccountID, CustomerID, AccountType, Balance, LastModified)
    VALUES (aID, cID, aType, initBal, CURDATE());
    SELECT 'Account opened.' AS Status;
END$$

CREATE PROCEDURE AccountOperations_CloseAccount(IN aID INT)
BEGIN
    DELETE FROM Accounts WHERE AccountID = aID;
    SELECT CONCAT('Account ', aID, ' closed.') AS Status;
END$$

CREATE FUNCTION AccountOperations_TotalBalance(cID INT)
RETURNS DECIMAL(10,2) DETERMINISTIC
BEGIN
    DECLARE total DECIMAL(10,2);
    SELECT SUM(Balance) INTO total FROM Accounts WHERE CustomerID = cID;
    RETURN IFNULL(total, 0);
END$$
DELIMITER ;

CALL AccountOperations_OpenAccount(5, 1, 'Savings', 2000);
SELECT AccountOperations_TotalBalance(1) AS TotalBalance;