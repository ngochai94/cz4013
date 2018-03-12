package cz4013.shared.response;

import cz4013.shared.currency.Currency;

/**
 * The response to a request to query information
 */
public class QueryResponse {
  public String name;
  public Currency currency;
  public Double balance;
  public boolean success;
  public String errorMessage;

  public QueryResponse() {
  }

  public QueryResponse(String name, Currency currency, Double balance, boolean success, String errorMessage) {
    this.name = name;
    this.currency = currency;
    this.balance = balance;
    this.success = success;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "QueryResponse(" + name + ", " + currency + ", " + balance + ", " + success + ", " + errorMessage + ")";
  }

  public static QueryResponse failed(String errorMessage) {
    return new QueryResponse("", Currency.USD, 0.0, false, errorMessage);
  }
}
