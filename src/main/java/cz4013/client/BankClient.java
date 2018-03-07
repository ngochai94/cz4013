package cz4013.client;

import cz4013.shared.currency.Currency;
import cz4013.shared.request.*;
import cz4013.shared.response.*;
import cz4013.shared.rpc.RawMessage;
import cz4013.shared.rpc.Transport;
import cz4013.shared.serialization.Deserializer;

import java.net.SocketAddress;
import java.util.Optional;
import java.util.UUID;

public class BankClient {
  private static int PASSWORD_LENGTH = 6;
  private Transport client;
  private SocketAddress serverAddress;

  public BankClient(Transport client, SocketAddress serverAddress) {
    this.client = client;
    this.serverAddress = serverAddress;
  }

  public void runOpenAccountService() {
    System.out.println("Please input the following information to open an account");
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double balance = askAmount();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "openAccount"),
        new OpenAccountRequest(name, password, currency, balance)
      )
    );
    RawMessage msg = client.recv();
    Response<OpenAccountResponse> resp = Deserializer.deserialize(new Response<OpenAccountResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      System.out.printf("Successfully created a new bank account with number = %d\n", resp.body.get().accountNumber);
    }
  }

  public void runCloseAccountService() {
    System.out.println("Please input the following information to close an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "closeAccount"),
        new CloseAccountRequest(name, accountNumber, password)
      )
    );
    RawMessage msg = client.recv();
    Response<CloseAccountResponse> resp = Deserializer.deserialize(new Response<CloseAccountResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      CloseAccountResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.printf("Successfully closed bank account with number = %d\n", accountNumber);
      } else {
        System.out.printf("Failed to close bank account with reason: %s\n", respBody.errorMessage);
      }
    }
  }

  public void runDepositService() {
    System.out.println("Please input the following information to deposit to an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "deposit"),
        new DepositRequest(name, accountNumber, password, currency, amount)
      )
    );
    RawMessage msg = client.recv();
    Response<DepositResponse> resp = Deserializer.deserialize(new Response<DepositResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      DepositResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.printf("Successfully deposit, new balance = %f %s\n", respBody.balance, respBody.currency);
      } else {
        System.out.printf("Failed to deposit with reason: %s\n", respBody.errorMessage);
      }
    }
  }

  public void runWithdrawService() {
    System.out.println("Please input the following information to withdraw from an account");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    Currency currency = askCurrency();
    Double amount = askAmount();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "deposit"),
        new DepositRequest(name, accountNumber, password, currency, -amount)
      )
    );
    RawMessage msg = client.recv();
    Response<DepositResponse> resp = Deserializer.deserialize(new Response<DepositResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      DepositResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.printf("Successfully withdraw, new balance = %f %s\n", respBody.balance, respBody.currency);
      } else {
        System.out.printf("Failed to withdraw with reason: %s\n", respBody.errorMessage);
      }
    }
  }

  public void runMonitorService() {
    System.out.print("Monitor interval (s) = ");
    double interval = Util.safeReadDouble();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "monitor"),
        new MonitorRequest(interval)
      )
    );
    RawMessage msg = client.recv();
    Response<MonitorStatusResponse> resp = Deserializer.deserialize(new Response<MonitorStatusResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      MonitorStatusResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.println("Successfully requested to monitor, waiting for update...");
        // TODO: block and wait for updates
      } else {
        System.out.println("Failed to request to monitor");
      }
    }
  }

  public void runQueryService() {
    System.out.println("Please input the following information to query from an account");
    int accountNumber = askAccountNumber();
    String password = askPassword();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "query"),
        new QueryRequest(accountNumber, password)
      )
    );
    RawMessage msg = client.recv();
    Response<QueryResponse> resp = Deserializer.deserialize(new Response<QueryResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      QueryResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.printf("Successfully queried, name = %s, balance = %f %s\n", respBody.name, respBody.balance, respBody.currency);
      } else {
        System.out.printf("Failed to query with reason: %s\n", respBody.errorMessage);
      }
    }
  }

  public void runMaintenanceService() {
    System.out.println("Please input the following information to pay the maintenance fee");
    int accountNumber = askAccountNumber();
    String name = askName();
    String password = askPassword();
    client.send(
      serverAddress,
      new Request<>(
        new RequestHeader(UUID.randomUUID(), "payMaintenanceFee"),
        new PayMaintenanceFeeRequest(accountNumber, name, password)
      )
    );
    RawMessage msg = client.recv();
    Response<PayMaintenanceFeeResponse> resp = Deserializer.deserialize(new Response<PayMaintenanceFeeResponse>() {}, msg.payload.get());
    ResponseHeader header = resp.header;
    if (header.status != Status.OK) {
      System.out.printf("Request failed with status = %s\n", header.status);
    } else {
      PayMaintenanceFeeResponse respBody = resp.body.get();
      if (respBody.success) {
        System.out.printf("Successfully pay maintenance fee, new balance = %f %s\n", respBody.balance, respBody.currency);
      } else {
        System.out.printf("Failed to pay maintenance fee with reason: %s\n", respBody.errorMessage);
      }
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
    int accountNumber = Util.safeReadInt();
    return accountNumber;
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
    return Util.safeReadDouble();
  }

}
