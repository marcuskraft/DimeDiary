package com.dimediary.port.in;

import com.dimediary.domain.ContinuousTransaction;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContinuousTransactionProvider {


  ContinuousTransaction persist(ContinuousTransaction continuousTransaction);

  void deleteAllContinuousTransactions(UUID continuousTransactionId);

  ContinuousTransaction getContinuousTransactions(UUID continuousTransactionId);

  List<ContinuousTransaction> loadContinuousTransactions(final UUID bankAccountId,
      final LocalDate dateFrom, final LocalDate dateUntil);

}