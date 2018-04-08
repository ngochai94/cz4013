package cz4013.server.service;

import cz4013.server.entity.AccountDetail;
import cz4013.server.storage.Database;
import cz4013.shared.request.*;
import cz4013.shared.response.*;
import cz4013.shared.rpc.Transport;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This class provides various banking services and handle interaction with DB.
 */
public class BankService {
  private Database db = new Database();
  private Transport transport;
  private Map<SocketAddress, Instant> listeners = new HashMap<>();
  private int nextAvailableAccountNumber = 1;

  /**
   * Constructs a {@code BankService}.
   *
   * @param transport transport layer to be used to send data for registered monitoring clients
   */
  public BankService(Transport transport) {
    this.transport = transport;
  }

  /**
   * Processes an open account request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public OpenAccountResponse processOpenAccount(OpenAccountRequest req) {
    int accountNumber = nextAvailableAccountNumber++;
    db.store(
      accountNumber,
      new AccountDetail(
        req.name,
        req.password,
        req.currency,
        req.balance
      )
    );
    broadcast(String.format("User %s opens new account with number %d", req.name, accountNumber));
    return new OpenAccountResponse(accountNumber);
  }

  /**
   * Processes a close account request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public CloseAccountResponse processCloseAccount(CloseAccountRequest req) {
    AccountDetail accountDetail = db.query(req.accountNumber);
    if (accountDetail == null) {
      return CloseAccountResponse.failed("This account number doesn't exist");
    }
    if (!accountDetail.name.equals(req.name)) {
      return CloseAccountResponse.failed("The account number is not under this name");
    }
    if (!accountDetail.password.equals(req.password)) {
      return CloseAccountResponse.failed("Wrong password");
    }
    db.delete(req.accountNumber);
    broadcast(String.format("User %s deletes account with number %d", req.name, req.accountNumber));
    return new CloseAccountResponse(true, "");
  }

  /**
   * Processes a deposit/withdraw request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public DepositResponse processDeposit(DepositRequest req) {
    AccountDetail accountDetail = db.query(req.accountNumber);
    if (accountDetail == null) {
      return DepositResponse.failed("This account number doesn't exist");
    }
    if (!accountDetail.name.equals(req.name)) {
      return DepositResponse.failed("The account number is not under this name");
    }
    if (!accountDetail.password.equals(req.password)) {
      return DepositResponse.failed("Wrong password");
    }
    if (accountDetail.currency != req.currency) {
      return DepositResponse.failed("The currency doesn't match");
    }
    if (accountDetail.amount + req.amount < 0) {
      return DepositResponse.failed("There's not enough balance to withdraw");
    }
    db.store(
      req.accountNumber,
      new AccountDetail(
        accountDetail.name,
        accountDetail.password,
        accountDetail.currency,
        accountDetail.amount + req.amount
      )
    );
    accountDetail = db.query(req.accountNumber);
    if (req.amount > 0)
      broadcast(String.format("User %s deposit %f %s to account %d", req.name, req.amount, req.currency, req.accountNumber));
    else
      broadcast(String.format("User %s withdraw %f %s from account %d", req.name, -req.amount, req.currency, req.accountNumber));
    return new DepositResponse(accountDetail.currency, accountDetail.amount, true, "");
  }

  /**
   * Processes a monitoring request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public MonitorStatusResponse processMonitor(MonitorRequest req, SocketAddress remote) {
    long interval = req.interval;
    listeners.put(remote, Instant.now().plusSeconds(interval));
    System.out.printf("User at %s starts to monitor for %d seconds\n", remote, interval);
    return new MonitorStatusResponse(true);
  }

  /**
   * Processes a query request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public QueryResponse processQuery(QueryRequest req) {
    AccountDetail accountDetail = db.query(req.accountNumber);
    if (accountDetail == null) {
      return QueryResponse.failed("This account number doesn't exist");
    }
    broadcast(String.format("Someone queries account %d", req.accountNumber));
    return new QueryResponse(accountDetail.name, accountDetail.currency, accountDetail.amount, true, "");
  }

  /**
   * Processes a pay maintenance fee request.
   *
   * @param req the request to be processed
   * @return the response after processing the given request
   */
  public PayMaintenanceFeeResponse processPayMaintenanceFee(PayMaintenanceFeeRequest req) {
    AccountDetail accountDetail = db.query(req.accountNumber);
    if (accountDetail == null) {
      return PayMaintenanceFeeResponse.failed("This account number doesn't exist");
    }
    db.store(
      req.accountNumber,
      new AccountDetail(
        accountDetail.name,
        accountDetail.password,
        accountDetail.currency,
        accountDetail.amount * 0.99
      )
    );
    accountDetail = db.query(req.accountNumber);
    broadcast(String.format("User %s pays maintenance fee for account %d", req.name, req.accountNumber));
    return new PayMaintenanceFeeResponse(accountDetail.currency, accountDetail.amount, true, "");
  }

  private void broadcast(String info) {
    purgeListeners();
    System.out.println(info);
    Response<MonitorUpdateResponse> resp = new Response<>(
      new ResponseHeader(UUID.randomUUID(), Status.OK),
      Optional.of(new MonitorUpdateResponse(info))
    );
    listeners.forEach((socketAddress, x) -> {
      transport.send(socketAddress, resp);
    });
  }

  private void purgeListeners() {
    listeners.entrySet().removeIf(x -> x.getValue().isBefore(Instant.now()));
  }
}
