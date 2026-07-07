package com.jkash.service;

import com.jkash.dao.BankAccountDAO;
import com.jkash.dao.TransactionDAO;
import com.jkash.model.BankAccount;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingService {

    private final Scanner scan = new Scanner(System.in);
    private final BankAccountDAO accountDAO = new BankAccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // CREATE ACCOUNT
    public void createAccount()
            throws SQLException
    {
        System.out.println("\n══════════════════════════════");
        System.out.println("       CREATE ACCOUNT");
        System.out.println("══════════════════════════════");

        String mobile;

        String pin;

        while (true)
        {

            System.out.print("Mobile Number : ");
            mobile = scan.nextLine().trim();

            if (!mobile.matches("\\d+"))
            {
                System.out.println("Invalid mobile number. Numbers only.");
                continue;
            }

            if (mobile.length() != 11)
            {
                System.out.println("Invalid mobile number.\nIt must contain exactly 11 digits.");
                continue;
            }

            if (!mobile.startsWith("09"))
            {
                System.out.println("Invalid mobile number.\nIt must start with 09.");
                continue;
            }

            if (accountDAO.mobileExists(mobile))
            {
                System.out.println("Mobile number already exists.");
                continue;
            }

            break;
        }

        while (true)
        {
            System.out.print("PIN (4 digits): ");
            pin = scan.nextLine().trim();

            if (!pin.matches("\\d+"))
            {
                System.out.println("Invalid PIN. Numbers only.");
                continue;
            }

            if (pin.length() != 4)
            {
                System.out.println("Invalid PIN.\nPIN must contain exactly 4 digits.");
                continue;
            }
            break;
        }

        System.out.print("Full Name : ");
        String fullName = scan.nextLine();

        BankAccount account = new BankAccount();

        account.setMobileNumber(mobile);
        String hashedPin = BCrypt.hashpw(pin, BCrypt.gensalt());

        account.setPin(hashedPin);
        account.setFullName(fullName);
        account.setBalance(0);

        boolean success = accountDAO.createAccount(account);

        if (success)
        {
            transactionDAO.addTransaction(mobile,"ACCOUNT",0,"Account Created");

            System.out.println("\nAccount Created Successfully!");
        }
    }

    public void login() throws SQLException {

        while (true) {

            System.out.println(
                    "\n══════════════════════════════");
            System.out.println(
                    "         JKASH LOGIN");
            System.out.println(
                    "══════════════════════════════");

            // MOBILE NUMBER
            System.out.print(
                    "Mobile Number : ");

            String mobile =
                    scan.nextLine().trim();

            // Validate format
            if (!mobile.matches("09\\d{9}")) {

                System.out.println(
                        "\nInvalid mobile number.");
                System.out.println(
                        "Please enter a valid " +
                                "11-digit Philippine mobile number.");

                continue;
            }

            // Find account
            BankAccount account =
                    accountDAO.findAccount(mobile);

            // Mobile not registered
            if (account == null) {

                System.out.println(
                        "\nMobile number is not registered.");

                continue;
            }

            // Account locked
            if (account.isLocked()) {

                System.out.println(
                        "\nThis account is locked.");

                return;
            }

            // PIN
            System.out.print(
                    "PIN : ");

            String pin =
                    scan.nextLine().trim();

            // Validate PIN format
            if (!pin.matches("\\d{4}")) {

                System.out.println(
                        "\nInvalid PIN.");
                System.out.println(
                        "PIN must contain exactly 4 digits.");

                continue;
            }

            // USE DAO LOGIN
            BankAccount user =
                    accountDAO.login(
                            mobile,
                            pin);

            // SUCCESS
            if (user != null) {

                transactionDAO
                        .addTransaction(
                                mobile,
                                "LOGIN",
                                0,
                                "User Logged In");

                System.out.println(
                        "\n══════════════════════════════");
                System.out.println(
                        "      LOGIN SUCCESSFUL");
                System.out.println(
                        "══════════════════════════════");
                System.out.println(
                        "Welcome, "
                                + user.getFullName());
                System.out.println(
                        "══════════════════════════════");

                bankingMenu(user);
                return;
            }

            // Reload account
            account =
                    accountDAO.findAccount(mobile);

            // Locked after 3 attempts
            if (account.isLocked()) {

                System.out.println("\n══════════════════════════════");
                System.out.println("      ACCOUNT LOCKED");
                System.out.println("══════════════════════════════");
                System.out.println("You entered an incorrect PIN 3 times.");
                System.out.println("Please contact support.");

                return;
            }

            int attemptsLeft = 3 - account.getFailedAttempts();

            System.out.println("\nIncorrect PIN.");
            System.out.println("Attempts Left : " + attemptsLeft);
        }
    }

    private void bankingMenu(BankAccount user)
            throws SQLException
    {

        int choice;

        do {

            System.out.println("\n══════════════════════════════");
            System.out.println("         JKASH APP");
            System.out.println("══════════════════════════════");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.println("══════════════════════════════");

            System.out.print("Choice : ");

            choice = Integer.parseInt(scan.nextLine());

            switch (choice)
            {
                case 1 -> checkBalance(user);

                case 2 -> deposit(user);

                case 3 -> withdraw(user);

                case 4 -> transfer(user);

                case 5 -> transactionDAO.viewTransactions(user.getMobileNumber());

                case 6 -> System.out.println("\nThank you for using JKash!");

                default -> System.out.println("\nInvalid Choice.");
            }

        } while (choice != 6);
    }

    private void checkBalance(
            BankAccount user) {

        System.out.println("\n══════════════════════════════");
        System.out.println("       ACCOUNT BALANCE");
        System.out.println("══════════════════════════════");

        System.out.println("Name : " + user.getFullName());
        System.out.printf("Balance : ₱%,.2f%n", user.getBalance());

        System.out.println("══════════════════════════════");
    }

    //Deposit

    private void deposit(
            BankAccount user)
            throws SQLException {

        System.out.print("\nEnter Amount : ₱");

        double amount = Double.parseDouble(scan.nextLine());

        if (amount <= 0)
        {
            System.out.println("Invalid Amount.");
            return;
        }

        double newBalance = user.getBalance() + amount;

        accountDAO.updateBalance(user.getMobileNumber(), newBalance);

        user.setBalance(newBalance);

        transactionDAO.addTransaction(user.getMobileNumber(), "DEPOSIT", amount, "Cash Deposit");

        System.out.println("\nDeposit Successful!");
        System.out.printf("New Balance : ₱%,.2f%n", newBalance);
    }

    // WITHDRAW
    private void withdraw(
            BankAccount user)
            throws SQLException {

        System.out.print("\nEnter Amount : ₱");

        double amount = Double.parseDouble(scan.nextLine());

        if (amount <= 0)
        {
            System.out.println("Invalid Amount.");
            return;
        }

        if (amount > user.getBalance())
        {
            System.out.println("Insufficient Balance.");
            return;
        }

        double newBalance = user.getBalance() - amount;

        accountDAO.updateBalance(user.getMobileNumber(), newBalance);

        user.setBalance(newBalance);

        transactionDAO.addTransaction(user.getMobileNumber(),"WITHDRAW", amount,"Cash Withdrawal");

        System.out.println("\nWithdrawal Successful!");

        System.out.printf("Remaining Balance : ₱%,.2f%n", newBalance);
    }

    // TRANSFER MONEY
    private void transfer(BankAccount user)
            throws SQLException {

        System.out.print(
                "\nReceiver Number : ");

        String receiver =
                scan.nextLine().trim();

        // Cannot transfer to yourself
        if (receiver.equals(
                user.getMobileNumber())) {

            System.out.println(
                    "\nYou cannot transfer money to your own account.");

            return;
        }

        BankAccount receiverAcc =
                accountDAO.findAccount(receiver);

        if (receiverAcc == null) {

            System.out.println(
                    "\nAccount not found.");

            return;
        }

        System.out.print("Amount : ₱");

        double amount = Double.parseDouble(scan.nextLine());

        if (amount <= 0)
        {
            System.out.println("Invalid Amount.");
            return;
        }

        if (amount > user.getBalance())
        {
            System.out.println("Insufficient Balance.");
            return;
        }

        double senderBalance = user.getBalance() - amount;

        double receiverBalance = receiverAcc.getBalance() + amount;

        accountDAO.updateBalance(user.getMobileNumber(), senderBalance);

        accountDAO.updateBalance(receiver, receiverBalance);

        user.setBalance(senderBalance);

        transactionDAO.addTransaction(user.getMobileNumber(),"TRANSFER", amount, "Sent to " + receiver);

        transactionDAO.addTransaction(receiver, "RECEIVED", amount, "Received from " + user.getMobileNumber());

        System.out.println("\nTransfer Successful!");

        System.out.printf("Remaining Balance : ₱%,.2f%n", senderBalance);
    }
}