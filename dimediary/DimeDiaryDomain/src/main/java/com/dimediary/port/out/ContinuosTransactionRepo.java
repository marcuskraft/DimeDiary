package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import java.util.List;

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
  void persist(ContinuousTransaction continuousTransaction);

  void delete(ContinuousTransaction continuousTransaction);

}