package cz4013.server.service;

import cz4013.server.entity.AccountDetail;
import cz4013.server.storage.Database;
import cz4013.shared.request.*;
import cz4013.shared.response.*;

public class BankService {
  private Database db = new Database();
  private int nextAvailableAccountNumber = 1;

  public Response<OpenAccountResponse> processOpenAccount(Request<OpenAccountRequest> request) {
    OpenAccountRequest openAccountRequest = request.body;
    int accountNumber = nextAvailableAccountNumber++;
    db.store(
      accountNumber,
      new AccountDetail(
        openAccountRequest.name,
        openAccountRequest.password,
        openAccountRequest.currency,
        openAccountRequest.balance
      )
    );
    return new Response<>(request.id, new OpenAccountResponse(accountNumber));
  }

  public Response<CloseAccountResponse> processCloseAccount(Request<CloseAccountRequest> request) {
    CloseAccountRequest closeAccountRequest = request.body;
    AccountDetail accountDetail = db.query(closeAccountRequest.accountNumber);
    if (accountDetail == null) {
      return new Response<>(request.id, CloseAccountResponse.failed("This account number doesn't exist"));
    }
    if (!accountDetail.name.equals(closeAccountRequest.name)) {
      return new Response<>(request.id, CloseAccountResponse.failed("The account number is not under this name"));
    }
    if (!accountDetail.password.equals(closeAccountRequest.password)) {
      return new Response<>(request.id, CloseAccountResponse.failed("Wrong password"));
    }
    db.delete(closeAccountRequest.accountNumber);
    return new Response<>(request.id, new CloseAccountResponse(true, ""));
  }

  public Response<DepositResponse> processDeposit(Request<DepositRequest> request) {
    DepositRequest depositRequest = request.body;
    AccountDetail accountDetail = db.query(depositRequest.accountNumber);
    if (accountDetail == null) {
      return new Response<>(request.id, DepositResponse.failed("This account number doesn't exist"));
    }
    if (!accountDetail.name.equals(depositRequest.name)) {
      return new Response<>(request.id, DepositResponse.failed("The account number is not under this name"));
    }
    if (!accountDetail.password.equals(depositRequest.password)) {
      return new Response<>(request.id, DepositResponse.failed("Wrong password"));
    }
    if (accountDetail.currency != depositRequest.currency) {
      return new Response<>(request.id, DepositResponse.failed("The currency doesn't match"));
    }
    if (accountDetail.amount + depositRequest.amount < 0) {
      return new Response<>(request.id, DepositResponse.failed("There's not enough balance to withdraw"));
    }
    db.store(
      depositRequest.accountNumber,
      new AccountDetail(
        accountDetail.name,
        accountDetail.password,
        accountDetail.currency,
        accountDetail.amount + depositRequest.amount
      )
    );
    return new Response<>(request.id, new DepositResponse(accountDetail.amount + depositRequest.amount, true, ""));
  }

  public Response<MonitorStatusResponse> processMonitor(Request<MonitorRequest> request) {
    double interval = request.body.interval;
    // TODO: store client's address
    return new Response<>(request.id, new MonitorStatusResponse(true));
  }
}
