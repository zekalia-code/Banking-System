package com.jkash.model;

public class Transaction {

    private int transactionId;
    private String mobileNumber;
    private String type;
    private double amount;
    private String details;
    private String transactionDate;

    public Transaction()
    {

    }

    public Transaction(int transactionId,
                       String mobileNumber,
                       String type,
                       double amount,
                       String details,
                       String transactionDate) {

        this.transactionId = transactionId;
        this.mobileNumber = mobileNumber;
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(int transactionId)
    {
        this.transactionId = transactionId;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public String getTransactionDate()
    {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate)
    {
        this.transactionDate = transactionDate;
    }
}