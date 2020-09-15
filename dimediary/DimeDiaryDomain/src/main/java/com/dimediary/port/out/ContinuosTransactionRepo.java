package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import java.util.List;
import java.util.UUID;

public interface ContinuosTransactionRepo {

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

  void delete(ContinuousTransaction continuousTransaction);

  ContinuousTransaction getContinuousTransaction(UUID continuousTransactionId);
}