package cz4013.server.entity;

import cz4013.shared.currency.Currency;

public class AccountDetail {
  public String name;
  public String password;
  public Currency currency;
  public double amount;

  public AccountDetail(String name, String password, Currency currency, double amount) {
    this.name = name;
    this.password = password;
    this.currency = currency;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "AccountDetail(" + name + ", " + password + ", " + currency + ", " + amount + ")";
  }
}
