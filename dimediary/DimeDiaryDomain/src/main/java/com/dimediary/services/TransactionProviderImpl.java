package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.BalanceProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.BankAccountRepo;
import com.dimediary.port.out.TransactionRepo;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionProviderImpl implements TransactionProvider {


  private final BalanceProvider balanceProvider;
  private final TransactionRepo transactionService;
  private final BankAccountRepo bankaccountRepo;


  @Autowired
  public TransactionProviderImpl(final BalanceProvider balanceProvider,
      final TransactionRepo transactionService,
      final BankAccountRepo bankaccountRepo) {
    this.balanceProvider = balanceProvider;
    this.transactionService = transactionService;
    this.bankaccountRepo = bankaccountRepo;
  }

  /**
   * @param dateFrom
   * @param dateUntil
   * @param bankAccount
   * @return list of transactions belonging to the given bank account between the two dates
   * (including both days)
   */
  @Override
  public List<Transaction> getTransactions(final LocalDate dateFrom, final LocalDate dateUntil,
      final BankAccount bankAccount) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);
    Validate.notNull(bankAccount);

    return this.transactionService.getTransactions(dateFrom, dateUntil, bankAccount);
  }

  @Override
  public List<Transaction> getTransactions(final LocalDate dateFrom, final LocalDate dateUntil,
      final String bankAccountName) {
    final BankAccount bankAccount = this.bankaccountRepo.getBankAccount(bankAccountName);
    return this.getTransactions(dateFrom, dateUntil, bankAccount);
  }

  /**
   * @param bankAccount
   * @return list of all transactions of the given bank account
   */
  @Override
  public List<Transaction> getTransactions(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    return this.transactionService.getTransactions(bankAccount);
  }

  /**
   * @param bankAccount
   * @param date
   * @return list of transactions at the given date for the given bank account
   */
  @Override
  public List<Transaction> getTransactions(final BankAccount bankAccount, final LocalDate date) {
    Validate.notNull(bankAccount);
    Validate.notNull(date);

    return this.transactionService.getTransactions(bankAccount, date);
  }

  /**
   * @param continuousTransaction
   * @param date
   * @return list of transactions belonging to this continuous transaction after the given date
   * (inclusive)
   */
  @Override
  public List<Transaction> getTransactionsFromDate(
      final ContinuousTransaction continuousTransaction,
      final LocalDate date) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(date);

    return this.transactionService.getTransactionsFromDate(continuousTransaction, date);
  }

  @Override
  public List<Transaction> getTransactionsUntil(final ContinuousTransaction continuousTransaction,
      final LocalDate dateUntil) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(dateUntil);

    return this.transactionService.getTransactionsUntil(continuousTransaction, dateUntil);
  }

  /**
   * @param continuousTransaction
   * @return list of all transactions belonging to the given continuous transaction
   */
  @Override
  public List<Transaction> getTransactions(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    return this.transactionService.getTransactions(continuousTransaction);
  }

  /**
   * @param dateFrom
   * @param dateUntil
   * @return all transactions in the given date range (both inclusive) with no bank account
   */
  @Override
  public List<Transaction> getTransactionsWithoutAccount(final LocalDate dateFrom,
      final LocalDate dateUntil) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);

    return this.transactionService.getTransactionsWithoutAccount(dateFrom, dateUntil);
  }

  /**
   * @param date
   * @return all transaction on the given date without a bank account
   */
  @Override
  public List<Transaction> getTrandactionsWithoutAccount(final LocalDate date) {
    Validate.notNull(date);

    return this.transactionService.getTrandactionsWithoutAccount(date);
  }

  @Override
  public Transaction persistTransaction(final Transaction transaction) {
    Validate.notNull(transaction, "Transaction must not be null");

    this.log.info("persist transaction: " + transaction.getId());

    final Transaction persistedTransaction = this.transactionService
        .persistTransaction(transaction);
    this.balanceProvider
        .updateBalance(persistedTransaction, BalanceProvider.BalanceAction.adding);

    return persistedTransaction;
  }

  @Override
  public void persistTransactions(final List<Transaction> transactions) {
    Validate.notNull(transactions, "Transactions must not be null");
    if (transactions.isEmpty()) {
      return;
    }

    for (final Transaction transaction : transactions) {
      this.persistTransaction(transaction);
    }

  }

  @Override
  public void delete(final Integer transactionId) {

    this.log.info("delete Transaction: " + transactionId);

    final Transaction transaction = this.transactionService.getTransaction(transactionId);

    this.delete(transaction);

  }

  @Override
  public void delete(final Transaction transaction) {
    this.balanceProvider
        .updateBalance(transaction, BalanceProvider.BalanceAction.deleting);
    this.transactionService.delete(transaction);
  }

  @Override
  public void deleteTransactions(final List<Transaction> transactions) {
    Validate.notNull(transactions, "transactions must not be null");
    if (transactions.isEmpty()) {
      return;

    }

    for (final Transaction transaction : transactions) {
      this.delete(transaction);
    }

  }

}
