package com.jkash.model;

public class User
{
    protected String mobileNumber;
    protected String fullName;

    public User()
    {

    }

    public User(String mobileNumber, String fullName)
    {

        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
}