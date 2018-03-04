package cz4013.server;

import cz4013.server.rpc.Router;
import cz4013.server.service.BankService;
import cz4013.shared.request.CloseAccountRequest;
import cz4013.shared.request.DepositRequest;
import cz4013.shared.request.MonitorRequest;
import cz4013.shared.request.OpenAccountRequest;

public class Main {
  public static void main(String[] args) {
    BankService svc = new BankService();
    Router r = new Router()
      .bind("openAccount", svc::processOpenAccount, new OpenAccountRequest() {})
      .bind("closeAccount", svc::processCloseAccount, new CloseAccountRequest() {})
      .bind("deposit", svc::processDeposit, new DepositRequest() {})
      .bind("monitor", svc::processMonitor, new MonitorRequest() {});
    System.out.print("Hello from server");
  }
}
