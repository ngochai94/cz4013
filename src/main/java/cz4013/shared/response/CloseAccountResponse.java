package cz4013.shared.response;

public class CloseAccountResponse {
  public boolean success;
  public String errorMessage;

  public CloseAccountResponse() {
  }

  public CloseAccountResponse(boolean success, String errorMessage) {
    this.success = success;
    this.errorMessage = errorMessage;
  }
}
