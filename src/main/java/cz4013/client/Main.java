package cz4013.client;

import cz4013.shared.currency.Currency;

import java.util.Optional;
import java.util.Scanner;

public class Main {
  private static int PASSWORD_LENGTH = 6;
  private static Scanner reader = new Scanner(System.in);

  public static void main(String[] args) {
    boolean shouldStop = false;
    while (!shouldStop) {
      int userChoice = askUserChoice();
      switch (userChoice) {
        case 1: runOpenAccountService();
          break;
        case 2: runCloseAccountService();
          break;
        case 3: runDepositService();
          break;
        case 4: runWithdrawService();
          break;
        case 5: runMonitorService();
          break;
        default: shouldStop = true;
          break;
      }
    }
    reader.close();
    System.out.println("Stopping client...");
  }

  private static int askUserChoice() {
    System.out.print("\n----------------------------------------------------------------\n" +
      "Please choose a service by typing [1-5]:\n" +
      "1: Open a new bank account\n" +
      "2: Close a bank account\n" +
      "3: Deposit to a bank account\n" +
      "4: Withdraw from a bank account\n" +
      "5: Monitor update from other accounts\n" +
      "0: Stop the client\n" +
      "Your choice = ");
    int choice = safeReadInt();
    if (choice < 0 || choice > 5) {
      System.out.println("Invalid choice!");
      return askUserChoice();
    }
    return choice;
  }

  private static void runOpenAccountService() {
    System.out.println("Please input the following information");
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double balance = askAmount();
  }

  private static void runCloseAccountService() {
    System.out.println("Please input the following information");
    String name = askName();
    int accountNumber = askAccountNumber();
    String password = askPassword();
  }

  private static void runDepositService() {
    System.out.println("Please input the following information");
    String name = askName();
    int accountNumber = askAccountNumber();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();
  }

  private static void runWithdrawService() {
    System.out.println("Please input the following information");
    String name = askName();
    int accountNumber = askAccountNumber();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();
  }

  private static void runMonitorService() {
    System.out.println("Monitor interval (s) = ");
    double interval = safeReadDouble();
  }

  private static String askName() {
    System.out.print("Your name = ");
    return reader.nextLine();
  }

  private static String askPassword() {
    System.out.printf("Your password (%d characters) = ", PASSWORD_LENGTH);
    String password = reader.nextLine();
    if (password.length() != PASSWORD_LENGTH) {
      System.out.printf("Password must have exactly %d characters!\n", PASSWORD_LENGTH);
      return askPassword();
    }
    return password;
  }

  private static int askAccountNumber() {
    System.out.print("Your account number = ");
    int accountNumber = safeReadInt();
    return accountNumber;
  }

  private static Currency askCurrency() {
    System.out.printf("Your currency choice (%s) = ", Currency.getAllCurrenciesString());
    String currency = reader.nextLine().toUpperCase();
    Optional<Currency> currencyOpt = Currency.getAllCurrencies().filter(x -> x.toString().equals(currency)).findFirst();
    if (currencyOpt.isPresent()) {
      return currencyOpt.get();
    } else {
      System.out.println("Invalid currency code!");
      return askCurrency();
    }
  }

  private static Double askAmount() {
    System.out.print("Amount of money = ");
    return safeReadDouble();
  }

  private static int safeReadInt() {
    try {
      return Integer.parseInt(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input an integer number!");
      return safeReadInt();
    }
  }

  private static double safeReadDouble() {
    try {
      return Double.parseDouble(reader.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Please input a number!");
      return safeReadDouble();
    }
  }
}
