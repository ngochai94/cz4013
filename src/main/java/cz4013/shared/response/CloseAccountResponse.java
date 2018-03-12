package cz4013.shared.response;

/**
 * The response to a request to close a bank account
 */
public class CloseAccountResponse {
  public boolean success;
  public String errorMessage;

  public CloseAccountResponse() {
  }

  public CloseAccountResponse(boolean success, String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "CloseAccountResponse(" + success + ", " + errorMessage + ")";
  }

  public static CloseAccountResponse failed(String errorMessage) {
    return new CloseAccountResponse(false, errorMessage);
  }
}
