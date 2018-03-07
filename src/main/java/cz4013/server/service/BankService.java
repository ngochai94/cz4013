package cz4013.server.service;

import cz4013.server.entity.AccountDetail;
import cz4013.server.storage.Database;
import cz4013.shared.request.*;
import cz4013.shared.response.*;

import java.net.SocketAddress;

public class BankService {
  private Database db = new Database();
  private int nextAvailableAccountNumber = 1;

  public OpenAccountResponse processOpenAccount(OpenAccountRequest req) {
    System.out.println("Opening bank account");
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
    return new OpenAccountResponse(accountNumber);
  }

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
    return new CloseAccountResponse(true, "");
  }

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
    return new DepositResponse(accountDetail.currency, accountDetail.amount, true, "");
  }

  public MonitorStatusResponse processMonitor(MonitorRequest req, SocketAddress remote) {
    double interval = req.interval;
    // TODO: store client's address
    return new MonitorStatusResponse(true);
  }

  public QueryResponse processQuery(QueryRequest req) {
    AccountDetail accountDetail = db.query(req.accountNumber);
    if (accountDetail == null) {
      return QueryResponse.failed("This account number doesn't exist");
    }
    return new QueryResponse(accountDetail.name, accountDetail.currency, accountDetail.amount, true, "");
  }

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
    return new PayMaintenanceFeeResponse(accountDetail.currency, accountDetail.amount, true, "");
  }
}
