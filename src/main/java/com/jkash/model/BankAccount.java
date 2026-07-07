package com.jkash.model;

public class BankAccount extends User {

    private int accountId;
    private String pin;
    private double balance;

    // NEW FIELDS
    private int failedAttempts;
    private boolean locked;

    public BankAccount() {
    }

    public BankAccount(int accountId,
                       String mobileNumber,
                       String fullName,
                       String pin,
                       double balance,
                       int failedAttempts,
                       boolean locked)
    {

        super(mobileNumber, fullName);

        this.accountId = accountId;
        this.pin = pin;
        this.balance = balance;
        this.failedAttempts = failedAttempts;
        this.locked = locked;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // NEW GETTERS AND SETTERS

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}