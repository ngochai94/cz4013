package cz4013.server.storage;

import cz4013.server.entity.AccountDetail;

import java.util.HashMap;

/**
 * An in-memory key-value based database which holds bank accounts records.
 * key = account number
 * value = account detail
 */
public class Database {
  private HashMap<Integer, AccountDetail> db = new HashMap<Integer, AccountDetail>();

  /**
   * Stores a bank account record.
   * If the accountNumber already exists, it will be overwritten.
   * @param accountNumber number of the account
   * @param accountDetail detail of the account
   */
  public void store(int accountNumber, AccountDetail accountDetail) {
    db.put(accountNumber, accountDetail);
  }

  /**
   * Deletes a bank account record.
   *
   * @param accountNumber number of the account
   */
  public void delete(int accountNumber) {
    db.remove(accountNumber);
  }

  /**
   * Gets information for a bank account.
   *
   * @param accountNumber number of the account
   * @return detail of the account
   */
  public AccountDetail query(int accountNumber) {
    return db.get(accountNumber);
  }
}
