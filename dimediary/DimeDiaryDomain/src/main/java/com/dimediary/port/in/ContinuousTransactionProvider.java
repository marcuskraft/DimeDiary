package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.util.List;
import java.util.UUID;

public interface ContinuousTransactionProvider {

  /**
   * @param bankAccount
   * @return all ContinuousTransactions belonging to this account
   */
  List<ContinuousTransaction> getContinuousTransactions(BankAccount bankAccount);

  /**
   * persists the given continuous transaction
   *
   * @param continuousTransaction
   */
  ContinuousTransaction persist(ContinuousTransaction continuousTransaction);

  void merge(ContinuousTransaction continuousTransaction);

  void deleteAllContinuousTransactions(ContinuousTransaction continuousTransaction);

  /**
   * generates all transactions belonging to this continuous transaction
   *
   * @param continuousTransaction
   * @return
   */
  List<Transaction> generateTransactionsForContinuousTransaction(
      ContinuousTransaction continuousTransaction);

  void deleteAllContinuousTransactions(UUID continuousTransactionId);

  ContinuousTransaction getContinuousTransactions(UUID continuousTransactionId);
}