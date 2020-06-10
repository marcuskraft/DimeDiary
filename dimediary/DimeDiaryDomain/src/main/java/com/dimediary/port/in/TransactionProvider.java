package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface TransactionProvider {

  /**
   * @param dateFrom
   * @param dateUntil
   * @param bankAccount
   * @return list of transactions belonging to the given bank account between the two dates
   * (including both days)
   */
  List<Transaction> getTransactions(LocalDate dateFrom, LocalDate dateUntil,
      BankAccount bankAccount);

  /**
   * @param bankAccount
   * @return list of all transactions of the given bank account
   */
  List<Transaction> getTransactions(BankAccount bankAccount);

  /**
   * @param bankAccount
   * @param date
   * @return list of transactions at the given date for the given bank account
   */
  List<Transaction> getTransactions(BankAccount bankAccount, LocalDate date);

  /**
   * @param continuousTransaction
   * @param date
   * @return list of transactions belonging to this continuous transaction after the given date
   * (inclusive)
   */
  List<Transaction> getTransactionsFromDate(ContinuousTransaction continuousTransaction,
      LocalDate date);

  List<Transaction> getTransactionsUntil(ContinuousTransaction continuousTransaction,
      LocalDate dateUntil);

  /**
   * @param continuousTransaction
   * @return list of all transactions belonging to the given continuous transaction
   */
  List<Transaction> getTransactions(ContinuousTransaction continuousTransaction);

  /**
   * @param dateFrom
   * @param dateUntil
   * @return all transactions in the given date range (both inclusive) with no bank account
   */
  List<Transaction> getTransactionsWithoutAccount(LocalDate dateFrom, LocalDate dateUntil);

  /**
   * @param date
   * @return all transaction on the given date without a bank account
   */
  List<Transaction> getTrandactionsWithoutAccount(LocalDate date);

  void persistTransaction(Transaction transaction);

  void persistTransactions(List<Transaction> transactions);

  void delete(Transaction transaction);

  void deleteTransactions(List<Transaction> transactions);

}