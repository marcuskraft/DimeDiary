package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.ContinuosTransactionRepo;
import com.dimediary.utils.date.DateUtils;
import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContinuousTransactionProviderImpl implements ContinuousTransactionProvider {


  private final TransactionProvider transactionProvider;


  private final ContinuosTransactionRepo continuosTransactionService;

  @Autowired
  public ContinuousTransactionProviderImpl(
      final TransactionProvider transactionProvider,
      final ContinuosTransactionRepo continuosTransactionService) {
    this.transactionProvider = transactionProvider;
    this.continuosTransactionService = continuosTransactionService;
  }

  /**
   * @param bankAccount
   * @return all ContinuousTransactions belonging to this account
   */
  @Override
  public List<ContinuousTransaction> getContinuousTransactions(final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    return this.continuosTransactionService.getContinuousTransactions(bankAccount);
  }

  /**
   * persists the given continuous transaction
   *
   * @param continuousTransaction
   */
  @Override
  public void persist(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    this.continuosTransactionService.persist(continuousTransaction);
  }

  /**
   * persists the given continuous transaction and a list of transactions
   *
   * @param continuousTransaction
   * @param transactions
   */
  @Override
  public void persistContinuousTransaction(final ContinuousTransaction continuousTransaction,
      final List<Transaction> transactions) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(transactions);
    if (transactions.isEmpty()) {
      return;
    }

    this.persist(continuousTransaction);
    this.transactionProvider.persistTransactions(transactions);


  }

  @Override
  public void merge(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    this.continuosTransactionService.persist(continuousTransaction);
  }

  @Override
  public void deleteAllContinuousTransactions(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    final List<Transaction> transactions = this.transactionProvider
        .getTransactions(continuousTransaction);
    this.transactionProvider.deleteTransactions(transactions);
    this.continuosTransactionService.delete(continuousTransaction);


  }

  /**
   * generates all transactions belonging to this continuous transaction
   *
   * @param continuousTransaction
   * @return
   */
  @Override
  public List<Transaction> generateTransactionsForContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    final RecurrenceRule recurrenceRule = RecurrenceRuleUtils
        .createRecurrenceRule(continuousTransaction.getRecurrenceRule());
    final LocalDate firstDate = continuousTransaction.getDateBegin();
    final List<LocalDate> dates = RecurrenceRuleUtils.getDatesForRecurrenceRule(recurrenceRule,
        DateUtils.localDateToDateTime(continuousTransaction.getDateBegin()),
        DateUtils.localDateToDateTime(firstDate));

    final List<Transaction> transactions = new ArrayList<>();
    for (final LocalDate date : dates) {
      transactions.add(continuousTransaction.createTransaction(date));
    }

    return transactions;
  }

}
