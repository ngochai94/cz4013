package cz4013.server.storage;

import cz4013.server.entity.AccountDetail;

import java.util.HashMap;

/**
 * Simulate a key-value database to hold bank accounts record
 * key = account number
 * valule = account detail
 */
public class Database {
  private HashMap<Integer, AccountDetail> db = new HashMap<Integer, AccountDetail>();

  public void store(int accountNumber, AccountDetail accountDetail) {
    db.put(accountNumber, accountDetail);
  }

  public void delete(int accountNumber) {
    db.remove(accountNumber);
  }

  public AccountDetail query(int accountNumber) {
    return db.get(accountNumber);
  }
}
