package com.dimediary.port.in;

import com.dimediary.domain.ContinuousTransaction;
import java.util.UUID;

public interface ContinuousTransactionProvider {


  ContinuousTransaction persist(ContinuousTransaction continuousTransaction);

  void deleteAllContinuousTransactions(ContinuousTransaction continuousTransaction);

  void deleteAllContinuousTransactions(UUID continuousTransactionId);

  ContinuousTransaction getContinuousTransactions(UUID continuousTransactionId);
}