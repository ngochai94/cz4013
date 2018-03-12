package cz4013.shared.request;

/**
 * The request to pay maintenance fee for a bank account
 */
public class PayMaintenanceFeeRequest {
  public int accountNumber;
  public String name;
  public String password;

  public PayMaintenanceFeeRequest() {
  }

  public PayMaintenanceFeeRequest(int accountNumber, String name, String password) {
    this.accountNumber = accountNumber;
    this.name = name;
    this.password = password;
  }

  @Override
  public String toString() {
    return "PayMaintenanceFeeRequest(" + accountNumber + ", " + name + ", " + password + ")";
  }
}
