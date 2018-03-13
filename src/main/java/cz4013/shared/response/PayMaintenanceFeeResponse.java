package cz4013.shared.response;

import cz4013.shared.currency.Currency;

/**
 * The response to a pay maintenance free request.
 */
public class PayMaintenanceFeeResponse {
  public Currency currency;
  public Double balance;
  public boolean success;
  public String errorMessage;

  public PayMaintenanceFeeResponse() {
  }

  public PayMaintenanceFeeResponse(Currency currency, Double balance, boolean success, String errorMessage) {
    this.currency = currency;
    this.balance = balance;
    this.success = success;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "PayMaintenanceFeeResponse(" + balance + ", " + success + ", " + errorMessage + ")";
  }

  public static PayMaintenanceFeeResponse failed(String errorMessage) {
    return new PayMaintenanceFeeResponse(Currency.USD, 0.0, false, errorMessage);
  }
}
