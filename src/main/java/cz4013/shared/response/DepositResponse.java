package cz4013.shared.response;

import cz4013.shared.currency.Currency;

/**
 * The response to a deposit request.
 */
public class DepositResponse {
  public Currency currency;
  public double balance;
  public boolean success;
  public String errorMessage;

  public DepositResponse() {
  }

  public DepositResponse(Currency currency, double balance, boolean success, String errorMessage) {
    this.currency = currency;
    this.balance = balance;
    this.success = success;
    this.errorMessage = errorMessage;
  }

  public static DepositResponse failed(String errorMessage) {
    return new DepositResponse(Currency.USD, 0, false, errorMessage);
  }

  @Override
  public String toString() {
    return "DepositResponse(" + currency + ", " + balance + ", " + success + ", " + errorMessage + ")";
  }
}
