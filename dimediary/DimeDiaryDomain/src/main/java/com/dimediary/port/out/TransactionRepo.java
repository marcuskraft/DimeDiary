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

  List<Transaction> getTransactions(BankAccount bankAccount);

  List<Transaction> getTransactions(BankAccount bankAccount, LocalDate date);

  List<Transaction> getTransactionsFromDate(ContinuousTransaction continuousTransaction,
      LocalDate date);

  List<Transaction> getTransactionsUntil(ContinuousTransaction continuousTransaction,
      LocalDate dateUntil);

  List<Transaction> getTransactions(ContinuousTransaction continuousTransaction);

  List<Transaction> getTransactionsWithoutAccount(LocalDate dateFrom, LocalDate dateUntil);

  List<Transaction> getTrandactionsWithoutAccount(LocalDate date);

  Transaction persistTransaction(final Transaction transaction);

  void persistTransactions(final List<Transaction> transactions);

  void delete(final Transaction transaction);

  void deleteTransactions(final List<Transaction> transactions);

}