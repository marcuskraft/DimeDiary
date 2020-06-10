package com.dimediary.port.in;

import com.dimediary.domain.helper.TransactionDialogStatus;

public interface TransactionGenerator {

  void generateTransactions(TransactionDialogStatus transactionDialogStatus);

}
