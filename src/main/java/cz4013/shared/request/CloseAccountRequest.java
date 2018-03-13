package cz4013.shared.request;

/**
 * The request to close a bank account.
 */
public class CloseAccountRequest {
  public String name;
  public int accountNumber;
  public String password;

  public CloseAccountRequest() {
  }

  public CloseAccountRequest(String name, int accountNumber, String password) {
    this.name = name;
    this.accountNumber = accountNumber;
    this.password = password;
  }

  @Override
  public String toString() {
    return "CloseAccountRequest(" + name + ", " + accountNumber + ", " + password + ")";
  }
}
