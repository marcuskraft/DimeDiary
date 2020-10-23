package com.dimediary.services;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.port.out.ContinuousTransactionRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContinuousTransactionProviderImpl implements ContinuousTransactionProvider {


  private final TransactionProvider transactionProvider;
  private final ContinuousTransactionRepo continuousTransactionService;
  private final RecurrenceRuleService recurrenceRuleService;

  @Autowired
  public ContinuousTransactionProviderImpl(
      final TransactionProvider transactionProvider,
      final ContinuousTransactionRepo continuousTransactionService,
      final RecurrenceRuleService recurrenceRuleService) {
    this.transactionProvider = transactionProvider;
    this.continuousTransactionService = continuousTransactionService;
    this.recurrenceRuleService = recurrenceRuleService;
  }


  @Override
  public ContinuousTransaction persist(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    final List<LocalDate> localDatesThatShouldExist = this
        .getDatesForContinuousTransaction(continuousTransaction);
    if (continuousTransaction.getId() != null) {
      return this.updateContinuousTransaction(continuousTransaction, localDatesThatShouldExist);
    } else {
      return this.persistNewContinuousTransaction(continuousTransaction, localDatesThatShouldExist);
    }

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

  @Override
  public List<ContinuousTransaction> loadContinuousTransactions(final UUID bankAccountId,
      final LocalDate dateFrom, final LocalDate dateUntil) {
    return this.continuousTransactionService
        .getContinuousTransactions(bankAccountId).stream().filter(continuousTransaction -> {
          if (continuousTransaction.getDateBegin().isAfter(dateUntil)) {
            return false;
          }
          return this.getDatesForContinuousTransactionFromUntil(continuousTransaction, dateFrom,
              dateUntil).size() > 0;
        }).collect(Collectors.toList());
  }

  private ContinuousTransaction persistNewContinuousTransaction(
      final ContinuousTransaction continuousTransaction,
      final List<LocalDate> localDatesThatShouldExist) {
    final List<Transaction> transactions = this
        .generateTransactionsForContinuousTransaction(continuousTransaction,
            localDatesThatShouldExist);
    return this.persistContinuousTransaction(continuousTransaction, transactions);
  }

  private ContinuousTransaction updateContinuousTransaction(
      final ContinuousTransaction continuousTransaction,
      final List<LocalDate> localDatesThatShouldExist) {
    final List<Transaction> oldTransactions = this.transactionProvider
        .getTransactionsForContinuousTransaction(continuousTransaction.getId());

    final List<Transaction> transactionsToDelete = oldTransactions.stream()
        .filter(transaction -> localDatesThatShouldExist.stream()
            .noneMatch(localDate -> localDate.equals(transaction.getDate())))
        .collect(Collectors.toList());

    final List<Transaction> transactionsToSave = localDatesThatShouldExist.stream().filter(
        localDate -> oldTransactions.stream()
            .noneMatch(transaction -> localDate.equals(transaction.getDate())))
        .map(continuousTransaction::createTransaction).collect(Collectors.toList());

    this.transactionProvider.deleteTransactions(transactionsToDelete);
    this.transactionProvider.persistTransactions(transactionsToSave);
    return this.continuousTransactionService.persist(continuousTransaction);
  }

  private void deleteAllContinuousTransactions(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    final List<Transaction> transactions = this.transactionProvider
        .getTransactionsForContinuousTransaction(continuousTransaction.getId());
    this.transactionProvider.deleteTransactions(transactions);
    this.continuousTransactionService.delete(continuousTransaction);


  }

  private List<LocalDate> getDatesForContinuousTransactionFromUntil(
      final ContinuousTransaction continuousTransaction, final LocalDate dateFrom,
      final LocalDate dateUntil) {
    return this.recurrenceRuleService
        .getDatesForRecurrenceSettings(continuousTransaction.getRecurrenceRule(),
            dateFrom, continuousTransaction.getRecurrenceExceptions(),
            continuousTransaction.getRecurrenceExtraInstances(), dateUntil);
  }

  private List<LocalDate> getDatesForContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    return this.recurrenceRuleService
        .getDatesForRecurrenceSettings(continuousTransaction.getRecurrenceRule(),
            continuousTransaction.getDateBegin(), continuousTransaction.getRecurrenceExceptions(),
            continuousTransaction.getRecurrenceExtraInstances());
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
      final ContinuousTransaction continuousTransaction, final List<LocalDate> dates) {
    final List<Transaction> transactions = new ArrayList<>();
    for (final LocalDate date : dates) {
      transactions.add(continuousTransaction.createTransaction(date));
    }

    return transactions;
  }

}
