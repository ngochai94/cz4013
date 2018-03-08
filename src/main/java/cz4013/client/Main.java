package cz4013.client;

import cz4013.shared.container.BufferPool;
import cz4013.shared.rpc.Transport;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Map;

public class Main {
  private static Transport client;
  private static SocketAddress serverAddress;

  public static void main(String[] args) throws SocketException {
    Map<String, String> env = System.getenv();
    String clientHost = env.getOrDefault("CLIENT_HOST", "127.0.0.1");
    String serverHost = env.getOrDefault("SERVER_HOST", "127.0.0.1");
    int clientPort = Integer.parseInt(env.getOrDefault("CLIENT_PORT", "12741"));
    int serverPort = Integer.parseInt(env.getOrDefault("SERVER_PORT", "12740"));
    BufferPool pool = new BufferPool(8192, 1024);
    client = new Transport(new DatagramSocket(new InetSocketAddress(clientHost, clientPort)), pool);
    serverAddress = new InetSocketAddress(serverHost, serverPort);

    BankClient bankClient = new BankClient(client, serverAddress);

    boolean shouldStop = false;
    while (!shouldStop) {
      int userChoice = askUserChoice();
      switch (userChoice) {
        case 1: bankClient.runOpenAccountService();
          break;
        case 2: bankClient.runCloseAccountService();
          break;
        case 3: bankClient.runDepositService();
          break;
        case 4: bankClient.runWithdrawService();
          break;
        case 5: bankClient.runMonitorService();
          break;
        case 6: bankClient.runQueryService();
          break;
        case 7: bankClient.runMaintenanceService();
          break;
        default: shouldStop = true;
          break;
      }
    }
    Util.closeReader();
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
      "6: Query information from a bank account\n" +
      "7: Pay maintenance fee from a bank account\n" +
      "0: Stop the client\n" +
      "Your choice = ");
    int choice = Util.safeReadInt();
    if (choice < 0 || choice > 7) {
      System.out.println("Invalid choice!");
      return askUserChoice();
    }
    return choice;
  }
}
