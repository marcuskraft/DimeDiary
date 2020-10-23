package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepo {

  Transaction getTransaction(UUID transactionId);

  List<Transaction> getTransactions(LocalDate dateFrom, LocalDate dateUntil,
      BankAccount bankAccount);

  List<Transaction> getTransactions(BankAccount bankAccount, LocalDate date);

  List<Transaction> getTransactionsFromDate(ContinuousTransaction continuousTransaction,
      LocalDate date);

  List<Transaction> getTransactionsUntil(ContinuousTransaction continuousTransaction,
      LocalDate dateUntil);

  List<Transaction> getTransactions(UUID id);

  List<Transaction> getTransactionsWithoutAccount(LocalDate dateFrom, LocalDate dateUntil);

  Transaction persistTransaction(final Transaction transaction);

  void delete(final Transaction transaction);

}