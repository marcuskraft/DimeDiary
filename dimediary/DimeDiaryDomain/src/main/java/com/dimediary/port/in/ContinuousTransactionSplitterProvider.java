package com.dimediary.port.in;

import com.dimediary.domain.Transaction;

public interface ContinuousTransactionSplitterProvider {

  void splitContinuousTransaction(final Transaction transaction);

}