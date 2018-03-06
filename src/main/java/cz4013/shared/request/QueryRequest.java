package cz4013.shared.request;

public class QueryRequest {
  public int accountNumber;
  public String password;

  public QueryRequest() {
  }

  public QueryRequest(int accountNumber, String password) {
    this.accountNumber = accountNumber;
    this.password = password;
  }

  @Override
  public String toString() {
    return "QueryRequest(" + accountNumber + ", " + password + ")";
  }
}
