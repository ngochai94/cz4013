package cz4013.server;

import cz4013.server.rpc.Router;
import cz4013.server.service.BankService;
import cz4013.shared.container.BufferPool;
import cz4013.shared.container.LruCache;
import cz4013.shared.request.*;
import cz4013.shared.response.Response;
import cz4013.shared.rpc.RawMessage;
import cz4013.shared.rpc.Transport;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws SocketException {
    Map<String, String> env = System.getenv();
    String host = env.getOrDefault("HOST", "127.0.0.1");
    int port = Integer.parseInt(env.getOrDefault("PORT", "12740"));
    boolean atMostOnce = Integer.parseInt(env.getOrDefault("AT_MOST_ONCE", "0")) != 0;

    BankService svc = new BankService();
    Router r = new Router(new LruCache<>(atMostOnce ? 1024 : 0))
      .bind("openAccount", svc::processOpenAccount, new OpenAccountRequest() {})
      .bind("closeAccount", svc::processCloseAccount, new CloseAccountRequest() {})
      .bind("deposit", svc::processDeposit, new DepositRequest() {})
      .bind("monitor", svc::processMonitor, new MonitorRequest() {})
      .bind("query", svc::processQuery, new QueryRequest() {})
      .bind("payMaintenanceFee", svc::processPayMaintenanceFee, new PayMaintenanceFeeRequest() {});

    BufferPool pool = new BufferPool(8192, 1024);
    Transport server = new Transport(new DatagramSocket(new InetSocketAddress(host, port)), pool);
    System.out.printf("Listening on udp://%s:%d\n", host, port);
    for (; ; ) {
      try (RawMessage req = server.recv()) {
        Response<?> resp = r.route(req);
        server.send(req.remote, resp);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
