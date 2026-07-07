package com.jkash;

import com.jkash.service.BankingService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
            throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        BankingService service = new BankingService();

        int choice;

        do
        {
            while (true)
            {
                System.out.println("\n══════════════════════════════");
                System.out.println("            JKASH");
                System.out.println("══════════════════════════════");
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.println("══════════════════════════════");
                System.out.print("Choice: ");

                String input = scan.nextLine();

                try
                {
                    choice = Integer.parseInt(input);

                    if (choice < 0 || choice > 2)

                    {
                        System.out.println("\nInvalid choice.  \nPlease enter 0-2.");
                        continue;
                    }
                    break;

                }
                catch (NumberFormatException e)
                {
                    System.out.println("\nInvalid character.\nPlease enter numbers only (0-2).");
                }
            }

            switch (choice)
            {
                case 1:
                    service.createAccount();
                    break;

                case 2:
                    service.login();
                    break;

                case 0:
                    System.out.println("\n══════════════════════════════");
                    System.out.println(" Thank you for using JKASH!");
                    System.out.println("══════════════════════════════");
                    break;
            }

        } while (choice != 0);

        scan.close();
    }
}