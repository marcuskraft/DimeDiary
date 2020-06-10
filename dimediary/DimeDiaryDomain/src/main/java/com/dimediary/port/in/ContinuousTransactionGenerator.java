package com.dimediary.port.in;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.util.Date;

public interface ContinuousTransactionGenerator {

  void addContinuousTransaction(ContinuousTransaction continuousTransaction);

  void deleteContinuousTransaction(ContinuousTransaction continuousTransaction);

  void editContinuousTransaction(ContinuousTransaction continuousTransaction, Date changeFromDate,
      Date changeUntilDate);

  void editContinuousTransactionToTransaction(ContinuousTransaction continuousTransaction,
      Transaction transaction);

  void editTransactionToContinuousTransaction(Transaction transaction,
      ContinuousTransaction continuousTransaction);

}
