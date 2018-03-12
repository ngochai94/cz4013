package cz4013.shared.response;

/**
 * The response for a request to open a bank account
 */
public class OpenAccountResponse {
  public int accountNumber;

  public OpenAccountResponse() {
  }

  public OpenAccountResponse(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Override
  public String toString() {
    return "OpenAccountResponse(" + accountNumber + ")";
  }
}
