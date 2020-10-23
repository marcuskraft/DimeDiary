package com.dimediary.port.out;

import com.dimediary.domain.ContinuousTransaction;
import java.util.List;
import java.util.UUID;

public interface ContinuousTransactionRepo {


  List<ContinuousTransaction> getContinuousTransactions(UUID bankAccountId);

  ContinuousTransaction persist(ContinuousTransaction continuousTransaction);

  void delete(ContinuousTransaction continuousTransaction);

  ContinuousTransaction getContinuousTransaction(UUID continuousTransactionId);


}