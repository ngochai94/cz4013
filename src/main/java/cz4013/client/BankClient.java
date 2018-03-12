package cz4013.client;

import cz4013.shared.currency.Currency;
import cz4013.shared.request.*;
import cz4013.shared.response.*;

import java.time.Duration;
import java.util.Optional;

/**
 * This class provides client side service calls.
 */
public class BankClient {
  private static int PASSWORD_LENGTH = 6;
  private Client client;

  public BankClient(Client client) {
    this.client = client;
  }

  /**
   * Send a request to server to create a new bank account.
   */
  public void runOpenAccountService() {
    System.out.println("Please input the following information to open an account");
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double balance = askAmount();
    OpenAccountResponse resp = client.request(
      "openAccount",
      new OpenAccountRequest(name, password, currency, balance),
      new Response<OpenAccountResponse>() {}
    );
    System.out.printf("Successfully created a new bank account with number = %d\n", resp.accountNumber);
  }

  /**
   * Send a request to server to close a bank account.
   */
  public void runCloseAccountService() {
    System.out.println("Please input the following information to close an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    CloseAccountResponse resp = client.request(
      "closeAccount",
      new CloseAccountRequest(name, accountNumber, password),
      new Response<CloseAccountResponse>() {}
    );
    if (resp.success) {
      System.out.printf("Successfully closed bank account with number = %d\n", accountNumber);
    } else {
      System.out.printf("Failed to close bank account with reason: %s\n", resp.errorMessage);
    }
  }

  /**
   * Send a request to server to deposit to a bank account.
   */
  public void runDepositService() {
    System.out.println("Please input the following information to deposit to an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();
    DepositResponse resp = client.request(
      "deposit",
      new DepositRequest(name, accountNumber, password, currency, amount),
      new Response<DepositResponse>() {}
    );
    if (resp.success) {
      System.out.printf("Successfully deposit, new balance = %f %s\n", resp.balance, resp.currency);
    } else {
      System.out.printf("Failed to deposit with reason: %s\n", resp.errorMessage);
    }
  }

  /**
   * Send a request to server to withdraw from a bank account.
   */
  public void runWithdrawService() {
    System.out.println("Please input the following information to withdraw from an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();

    DepositResponse resp = client.request(
      "deposit",
      new DepositRequest(name, accountNumber, password, currency, -amount),
      new Response<DepositResponse>() {}
    );
    if (resp.success) {
      System.out.printf("Successfully withdraw, new balance = %f %s\n", resp.balance, resp.currency);
    } else {
      System.out.printf("Failed to withdraw with reason: %s\n", resp.errorMessage);
    }
  }

  /**
   * Send a request to server to monitor updates from other clients.
   */
  public void runMonitorService() {
    System.out.print("Monitor interval (s) = ");
    int interval = Util.safeReadInt();
    MonitorStatusResponse status = client.request(
      "monitor",
      new MonitorRequest(interval),
      new Response<MonitorStatusResponse>() {}
    );

    if (status.success) {
      System.out.println("Successfully requested to monitor, waiting for updates...");
      client.poll(
        new Response<MonitorUpdateResponse>() {},
        Duration.ofSeconds(interval),
        update -> System.out.println("Update: " + update.info)
      );
      System.out.println("Finished monitoring.");
    } else {
      System.out.println("Failed to request to monitor");
    }
  }

  /**
   * Send a request to server to get info of a bank account.
   */
  public void runQueryService() {
    System.out.println("Please input the following information to query from an account");
    int accountNumber = askAccountNumber();
    String password = askPassword();
    QueryResponse resp = client.request(
      "query",
      new QueryRequest(accountNumber, password),
      new Response<QueryResponse>() {}
    );
    if (resp.success) {
      System.out.printf("Successfully queried, name = %s, balance = %f %s\n", resp.name, resp.balance, resp.currency);
    } else {
      System.out.printf("Failed to query with reason: %s\n", resp.errorMessage);
    }
  }

  /**
   * Send a request to server to pay maintenance fee
   */
  public void runMaintenanceService() {
    System.out.println("Please input the following information to pay the maintenance fee");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();

    PayMaintenanceFeeResponse resp = client.request(
      "payMaintenanceFee",
      new PayMaintenanceFeeRequest(accountNumber, name, password),
      new Response<PayMaintenanceFeeResponse>() {}
    );
    if (resp.success) {
      System.out.printf("Successfully pay maintenance fee, new balance = %f %s\n", resp.balance, resp.currency);
    } else {
      System.out.printf("Failed to pay maintenance fee with reason: %s\n", resp.errorMessage);
    }
  }

  private String askName() {
    System.out.print("Your name = ");
    return Util.readLine();
  }

  private String askPassword() {
    System.out.printf("Your password (%d characters) = ", PASSWORD_LENGTH);
    String password = Util.readLine();
    if (password.length() != PASSWORD_LENGTH) {
      System.out.printf("Password must have exactly %d characters!\n", PASSWORD_LENGTH);
      return askPassword();
    }
    return password;
  }

  private int askAccountNumber() {
    System.out.print("Your account number = ");
    return Util.safeReadInt();
  }

  private Currency askCurrency() {
    System.out.printf("Your currency choice (%s) = ", Currency.getAllCurrenciesString());
    String currency = Util.readLine().toUpperCase();
    Optional<Currency> currencyOpt = Currency.getAllCurrencies().filter(x -> x.toString().equals(currency)).findFirst();
    if (currencyOpt.isPresent()) {
      return currencyOpt.get();
    } else {
      System.out.println("Invalid currency code!");
      return askCurrency();
    }
  }

  private Double askAmount() {
    System.out.print("Amount of money = ");
    double amount = Util.safeReadDouble();
    if (amount < 0) {
      System.out.println("Amount cannot be negative!");
      return askAmount();
    }
    return amount;
  }
}
