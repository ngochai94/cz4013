package cz4013.shared.request;

public class CloseAccountRequest {
  public int accountNumber;
  public String name;
  public String password;

  public CloseAccountRequest() {
  }

  public CloseAccountRequest(int accountNumber, String name, String password) {
    this.accountNumber = accountNumber;
    this.name = name;
    this.password = password;
  }

  @Override
  public String toString() {
    return "CloseAccountRequest(" + accountNumber + ", " + name + ", " + password + ")";
  }
}
