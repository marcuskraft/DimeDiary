package com.dimediary.port.in;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionProvider {


  List<Transaction> getTransactionsForContinuousTransaction(LocalDate dateFrom, LocalDate dateUntil,
      UUID bankAccountId);

  List<Transaction> getTransactionsFromDate(ContinuousTransaction continuousTransaction,
      LocalDate date);

  List<Transaction> getTransactionsUntil(ContinuousTransaction continuousTransaction,
      LocalDate dateUntil);

  List<Transaction> getTransactionsForContinuousTransaction(UUID continuousTransactionId);

  List<Transaction> getTransactionsWithoutAccount(LocalDate dateFrom, LocalDate dateUntil);

  Transaction getTransaction(UUID id);

  Transaction persistTransaction(Transaction transaction);

  void persistTransactions(List<Transaction> transactions);

  void delete(UUID transactionId);

  void deleteTransactions(List<Transaction> transactions);

}