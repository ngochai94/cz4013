package cz4013.shared.request;

import cz4013.shared.currency.Currency;

/**
 * The request to open a bank account
 */
public class OpenAccountRequest {
  public String name;
  public String password;
  public Currency currency;
  public double balance;

  public OpenAccountRequest() {
  }

  public OpenAccountRequest(String name, String password, Currency currency, double balance) {
    super();
    this.name = name;
    this.password = password;
    this.currency = currency;
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "OpenAccountRequest(" + name + ", " + password + ", " + currency + ", " + balance + ")";
  }
}
