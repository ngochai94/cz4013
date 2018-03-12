package cz4013.server.storage;

import cz4013.server.entity.AccountDetail;

import java.util.HashMap;

/**
 * Simulate a key-value database to hold bank accounts record
 * key = account number
 * value = account detail
 */
public class Database {
  private HashMap<Integer, AccountDetail> db = new HashMap<Integer, AccountDetail>();

  /**
   * Store a bank account record
   * If the accountNumber already exists in the db, it will be overwritten
   * @param accountNumber number of the account
   * @param accountDetail detail of the account
   */
  public void store(int accountNumber, AccountDetail accountDetail) {
    db.put(accountNumber, accountDetail);
  }

  /**
   * Delete a bank account record
   *
   * @param accountNumber number of the account
   */
  public void delete(int accountNumber) {
    db.remove(accountNumber);
  }

  /**
   * Get information for a bank account
   *
   * @param accountNumber number of the account
   * @return detail of the account
   */
  public AccountDetail query(int accountNumber) {
    return db.get(accountNumber);
  }
}
