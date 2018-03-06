package cz4013.server;

import cz4013.server.rpc.Router;
import cz4013.server.service.BankService;
import cz4013.shared.request.*;

public class Main {
  public static void main(String[] args) {
    BankService svc = new BankService();
    Router r = new Router()
      .bind("openAccount", svc::processOpenAccount, new OpenAccountRequest() {})
      .bind("closeAccount", svc::processCloseAccount, new CloseAccountRequest() {})
      .bind("deposit", svc::processDeposit, new DepositRequest() {})
      .bind("monitor", svc::processMonitor, new MonitorRequest() {})
      .bind("query", svc::processQuery, new QueryRequest() {})
      .bind("payMaintenanceFee", svc::processPayMaintenanceFee, new PayMaintenanceFeeRequest() {});
    System.out.print("Hello from server");
  }
}
