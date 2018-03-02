package cz4013.shared.response;

public class DepositResponse {
  public double balance;
  public boolean success;
  public String errorMessage;

  public DepositResponse() {
  }

  public DepositResponse(double balance, boolean success, String errorMessage) {
    this.balance = balance;
    this.success = success;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "DepositResponse(" + balance + ", " + success + ", " + errorMessage + ")";
  }

  public static DepositResponse failed(String errorMessage) {
    return new DepositResponse(0, false, errorMessage);
  }
}
