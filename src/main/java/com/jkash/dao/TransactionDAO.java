package com.jkash.dao;

import com.jkash.database.DBConnection;

import java.sql.*;

public class TransactionDAO
{

    public void addTransaction(
            String mobileNumber,
            String type,
            double amount,
            String details)
            throws SQLException {

        String sql = """
                INSERT INTO transactions
                (mobile_number,
                type,
                amount,
                details)
                VALUES (?, ?, ?, ?)
                """;

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, mobileNumber);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.setString(4, details);

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    //History

    public void viewTransactions(String mobileNumber)
            throws SQLException
    {
        String sql = """
                SELECT *
                FROM transactions
                WHERE mobile_number = ?
                ORDER BY transaction_date DESC
                """;

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, mobileNumber);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n════════════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                                  TRANSACTION HISTORY");
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════════════════════");

        System.out.printf(
                "%-5s %-12s %-15s %-25s %-35s %-42s %n",
                "ID",
                "TYPE",
                "AMOUNT",
                "DETAILS",
                "Date",
                "Time");

        System.out.println("--------------------------------------------------------------------------------------------------------------");

        boolean found = false;

        while (rs.next())
        {
            found = true;

            System.out.printf(
                    "%-5d %-12s ₱%-14.2f %-25s %-35s %-42s %n",
                    rs.getInt("transaction_id"),
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    rs.getString("details"),
                    rs.getDate("transaction_date"),
                    rs.getTime("transaction_date"));
        }

        if (!found)
        {
            System.out.println("No transactions found.");
        }

        System.out.println("═══════════════════════════════════════════════════════════════════════════════════════════════════════════════");

        rs.close();
        ps.close();
        con.close();
    }
}