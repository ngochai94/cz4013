package cz4013.shared.response;

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
