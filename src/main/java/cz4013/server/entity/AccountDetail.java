package cz4013.server.entity;

import cz4013.shared.currency.Currency;

/**
 * Details about a bank account stored in DB.
 */
public class AccountDetail {
  public String name;
  public String password;
  public Currency currency;
  public double amount;

  /**
   * Constructs an entry for a bank account in DB.
   *
   * @param name     name of the account owner
   * @param password password of this account
   * @param currency currency used by this account
   * @param amount   amount of money on this account
   */
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
