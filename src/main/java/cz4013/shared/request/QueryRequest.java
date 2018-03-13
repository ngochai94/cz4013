package cz4013.shared.request;

/**
 * The request to query information of a bank account.
 */
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
