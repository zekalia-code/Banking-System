package com.jkash.dao;

import com.jkash.database.DBConnection;
import com.jkash.model.BankAccount;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
public class BankAccountDAO {

    public boolean createAccount(BankAccount account)
            throws SQLException
    {
        String sql = """
                INSERT INTO bank_account
                (mobile_number, pin, full_name, balance)
                VALUES (?, ?, ?, ?)
                """;

        Connection cn = DBConnection.getConnection();

        PreparedStatement ps = cn.prepareStatement(sql);

        ps.setString(1, account.getMobileNumber());
        ps.setString(2, account.getPin());
        ps.setString(3, account.getFullName());
        ps.setDouble(4, account.getBalance());

        int rows = ps.executeUpdate();

        ps.close();
        cn.close();

        return rows > 0;
    }

    public BankAccount login(
            String mobile,
            String pin)
            throws SQLException
    {
        BankAccount account =
                findAccount(mobile);

        if (account == null)
        {
            System.out.println(
                    "Account not found.");

            return null;
        }

        if (account.isLocked())
        {
            System.out.println(
                    "Account is locked.");

            return null;
        }

        // BCrypt verification
        if (BCrypt.checkpw(
                pin,
                account.getPin()))
        {
            System.out.println(
                    "Correct PIN.");

            resetAttempts(mobile);

            return account;
        }

        System.out.println(
                "Wrong PIN.");

        incrementFailedAttempts(mobile);

        account = findAccount(mobile);

        if (account.getFailedAttempts() >= 3)
        {
            lockAccount(mobile);

            System.out.println(
                    "Account has been locked.");
        }

        return null;
    }

    public BankAccount findAccount(String mobileNumber)
            throws SQLException
    {
        String sql = """
                SELECT *
                FROM bank_account
                WHERE mobile_number = ?
                """;

        Connection cn = DBConnection.getConnection();

        PreparedStatement ps = cn.prepareStatement(sql);

        ps.setString(1, mobileNumber);

        ResultSet rs = ps.executeQuery();

        BankAccount account = null;

        if (rs.next())
        {
            account = new BankAccount();

            account.setAccountId(rs.getInt("account_id"));

            account.setMobileNumber(rs.getString("mobile_number"));

            account.setFullName(rs.getString("full_name"));

            account.setPin(rs.getString("pin"));

            account.setBalance(rs.getDouble("balance"));

            account.setFailedAttempts(rs.getInt("failed_attempts"));

            account.setLocked(rs.getBoolean("is_locked"));
        }

        rs.close();
        ps.close();
        cn.close();

        return account;
    }

    public void updateBalance(String mobileNumber, double newBalance)
            throws SQLException
    {
        String sql = """
                UPDATE bank_account
                SET balance = ?
                WHERE mobile_number = ?
                """;

        Connection cn = DBConnection.getConnection();

        PreparedStatement ps = cn.prepareStatement(sql);

        ps.setDouble(1, newBalance);
        ps.setString(2, mobileNumber);

        ps.executeUpdate();

        ps.close();
        cn.close();
    }

    public boolean mobileExists(
            String mobile)
            throws SQLException {

        String sql = """
                SELECT *
                FROM bank_account
                WHERE mobile_number = ?
                """;

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, mobile);

        ResultSet rs =
                ps.executeQuery();

        boolean exists =
                rs.next();

        rs.close();
        ps.close();
        con.close();

        return exists;
    }

    public void incrementFailedAttempts(
            String mobile)
            throws SQLException {

        System.out.println(
                "Incrementing attempts for: '"
                        + mobile
                        + "'");

        String sql = """
                UPDATE bank_account
                SET failed_attempts =
                    failed_attempts + 1
                WHERE mobile_number = ?
                """;

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, mobile);

        int rows =
                ps.executeUpdate();

        System.out.println(
                "Rows updated: "
                        + rows);

        ps.close();
        con.close();
    }

    public void resetAttempts(
            String mobile)
            throws SQLException {

        String sql = """
                UPDATE bank_account
                SET failed_attempts = 0
                WHERE mobile_number = ?
                """;

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, mobile);

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void lockAccount(
            String mobile)
            throws SQLException {

        String sql = """
                UPDATE bank_account
                SET is_locked = TRUE
                WHERE mobile_number = ?
                """;

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, mobile);

        ps.executeUpdate();

        ps.close();
        con.close();
    }
}