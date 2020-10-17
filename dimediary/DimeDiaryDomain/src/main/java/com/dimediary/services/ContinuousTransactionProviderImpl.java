package com.dimediary.services;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.exceptions.RecurrenceException;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.ContinuousTransactionRepo;
import com.dimediary.utils.date.DateUtils;
import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContinuousTransactionProviderImpl implements ContinuousTransactionProvider {


  private final TransactionProvider transactionProvider;
  private final ContinuousTransactionRepo continuousTransactionService;
  private final RecurrenceService recurrenceService;

  @Autowired
  public ContinuousTransactionProviderImpl(
      final TransactionProvider transactionProvider,
      final ContinuousTransactionRepo continuousTransactionService,
      final RecurrenceService recurrenceService) {
    this.transactionProvider = transactionProvider;
    this.continuousTransactionService = continuousTransactionService;
    this.recurrenceService = recurrenceService;
  }


  @Override
  public ContinuousTransaction persist(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    if (continuousTransaction.getId() != null) {
      this.deleteAllContinuousTransactions(continuousTransaction);
      continuousTransaction.setId(null);
    }

    final List<Transaction> transactions;
    try {
      transactions = this
          .generateTransactionsForContinuousTransaction(continuousTransaction);
    } catch (final InvalidRecurrenceRuleException e) {
      throw new RecurrenceException("error during calculation of the recurrence rule", e);
    }
    return this.persistContinuousTransaction(continuousTransaction, transactions);
  }


  @Override
  public void deleteAllContinuousTransactions(final UUID continuousTransactionId) {
    final ContinuousTransaction continuousTransaction = this.continuousTransactionService
        .getContinuousTransaction(continuousTransactionId);

    this.deleteAllContinuousTransactions(continuousTransaction);
  }

  @Override
  public ContinuousTransaction getContinuousTransactions(final UUID continuousTransactionId) {
    return this.continuousTransactionService.getContinuousTransaction(continuousTransactionId);
  }

  private void deleteAllContinuousTransactions(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    final List<Transaction> transactions = this.transactionProvider
        .getTransactions(continuousTransaction);
    this.transactionProvider.deleteTransactions(transactions);
    this.continuousTransactionService.delete(continuousTransaction);


  }


  private ContinuousTransaction persistContinuousTransaction(
      final ContinuousTransaction continuousTransaction,
      final List<Transaction> transactions) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(transactions);
    if (transactions.isEmpty()) {
      return null;
    }

    final ContinuousTransaction continuousTransactionRet = this.continuousTransactionService
        .persist(continuousTransaction);
    this.transactionProvider.persistTransactions(transactions);

    return continuousTransactionRet;
  }


  private List<Transaction> generateTransactionsForContinuousTransaction(
      final ContinuousTransaction continuousTransaction) throws InvalidRecurrenceRuleException {
    final RecurrenceRule recurrenceRule = this.recurrenceService
        .getRecurrenceRule(continuousTransaction.getRecurrenceSettings());
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
