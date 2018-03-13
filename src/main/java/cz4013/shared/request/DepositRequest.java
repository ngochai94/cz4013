package cz4013.shared.request;

import cz4013.shared.currency.Currency;

/**
 * The request to deposit to a bank account.
 */
public class DepositRequest {
  public String name;
  public int accountNumber;
  public String password;
  public Currency currency;
  public double amount;

  public DepositRequest() {
  }

  public DepositRequest(String name, int accountNumber, String password, Currency currency, double amount) {
    super();
    this.name = name;
    this.accountNumber = accountNumber;
    this.password = password;
    this.currency = currency;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "DepositRequest(" + name + ", " + accountNumber + ", " + password + ", " + currency + ", " + amount + ")";
  }
}
