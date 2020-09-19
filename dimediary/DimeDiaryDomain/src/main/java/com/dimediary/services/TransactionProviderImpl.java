package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.BalanceUseCase;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.BankAccountRepo;
import com.dimediary.port.out.TransactionRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionProviderImpl implements TransactionProvider {


  private final BalanceService balanceService;
  private final TransactionRepo transactionService;
  private final BankAccountRepo bankaccountRepo;


  @Autowired
  public TransactionProviderImpl(final BalanceService balanceService,
      final TransactionRepo transactionService,
      final BankAccountRepo bankaccountRepo) {
    this.balanceService = balanceService;
    this.transactionService = transactionService;
    this.bankaccountRepo = bankaccountRepo;
  }


  @Override
  public List<Transaction> getTransactions(final LocalDate dateFrom, final LocalDate dateUntil,
      final UUID bankAccountId) {
    final BankAccount bankAccount = this.bankaccountRepo.getBankAccount(bankAccountId);
    return this.getTransactions(dateFrom, dateUntil, bankAccount);
  }

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


  @Override
  public List<Transaction> getTransactions(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    return this.transactionService.getTransactions(continuousTransaction);
  }

  @Override
  public List<Transaction> getTransactionsWithoutAccount(final LocalDate dateFrom,
      final LocalDate dateUntil) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);

    return this.transactionService.getTransactionsWithoutAccount(dateFrom, dateUntil);
  }

  @Override
  public Transaction persistTransaction(final Transaction transaction) {
    Validate.notNull(transaction, "Transaction must not be null");

    TransactionProviderImpl.log.info("persist transaction: " + transaction.getId());

    final Transaction persistedTransaction = this.transactionService
        .persistTransaction(transaction);
    this.balanceService
        .updateBalance(persistedTransaction, BalanceUseCase.BalanceAction.adding);

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
  public void delete(final UUID transactionId) {

    TransactionProviderImpl.log.info("delete Transaction: " + transactionId);

    final Transaction transaction = this.transactionService.getTransaction(transactionId);

    this.delete(transaction);

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

  private List<Transaction> getTransactions(final LocalDate dateFrom, final LocalDate dateUntil,
      final BankAccount bankAccount) {
    Validate.notNull(dateFrom);
    Validate.notNull(dateUntil);
    Validate.notNull(bankAccount);

    return this.transactionService.getTransactions(dateFrom, dateUntil, bankAccount);
  }

  private void delete(final Transaction transaction) {
    this.balanceService
        .updateBalance(transaction, BalanceUseCase.BalanceAction.deleting);
    this.transactionService.delete(transaction);
  }

}
