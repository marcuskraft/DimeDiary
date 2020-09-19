package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import java.util.List;
import java.util.UUID;

public interface ContinuousTransactionRepo {


  List<ContinuousTransaction> getContinuousTransactions(BankAccount bankAccount);

  ContinuousTransaction persist(ContinuousTransaction continuousTransaction);

  void delete(ContinuousTransaction continuousTransaction);

  ContinuousTransaction getContinuousTransaction(UUID continuousTransactionId);
}