package com.dimediary.services;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.domain.helper.TransactionDialogStatus;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.ContinuousTransactionSplitterProvider;
import com.dimediary.port.in.TransactionGenerator;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.utils.date.DateUtils;
import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import java.time.LocalDate;
import java.util.List;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionGeneratorImpl implements TransactionGenerator {

  private final TransactionProvider transactionProvider;

  private final ContinuousTransactionSplitterProvider continuousTransactionSplitterProvider;

  private final ContinuousTransactionProvider continuousTransactionProvider;


  @Autowired
  public TransactionGeneratorImpl(final TransactionProvider transactionProvider,
      final ContinuousTransactionSplitterProvider continuousTransactionSplitterProvider,
      final ContinuousTransactionProvider continuousTransactionProvider) {
    this.transactionProvider = transactionProvider;
    this.continuousTransactionSplitterProvider = continuousTransactionSplitterProvider;
    this.continuousTransactionProvider = continuousTransactionProvider;
  }

  @Override
  public void generateTransactions(final TransactionDialogStatus transactionDialogStatus) {
    if (transactionDialogStatus.getTransaction() != null) {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.changeTransaction(transactionDialogStatus);
      } else {
        this.changeNewContinuousTransactionFromTransaction(transactionDialogStatus);
      }
    } else if (transactionDialogStatus.getContinuousTransaction() != null) {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.changeContinuesTransactionToTransaction(transactionDialogStatus);
      } else {
        this.changeContinuousTransaction(transactionDialogStatus);
      }
    } else {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.createNewTransaction(transactionDialogStatus);
      } else {
        this.createNewContinuousTransaction(transactionDialogStatus);
      }
    }
  }

  private void createNewContinuousTransaction(
      final TransactionDialogStatus transactionDialogStatus) {

    final ContinuousTransaction continuousTransaction = new ContinuousTransaction();
    this.setContinuousTransactionAttributtes(continuousTransaction, transactionDialogStatus);

    final List<Transaction> transactions = this.continuousTransactionProvider
        .generateTransactionsForContinuousTransaction(continuousTransaction);
    this.continuousTransactionProvider
        .persistContinuousTransaction(continuousTransaction, transactions);

  }

  private void createNewTransaction(final TransactionDialogStatus transactionDialogStatus) {

    final Transaction transaction = new Transaction();
    this.setTransactionAttributes(transaction, transactionDialogStatus);
    this.transactionProvider.persistTransaction(transaction);

  }

  private void setTransactionAttributes(final Transaction transaction,
      final TransactionDialogStatus transactionDialogStatus) {
    transaction.setAmount(transactionDialogStatus.getAmount());
    transaction.setBankAccount(transactionDialogStatus.getBankaccount());
    transaction.setCategory(transactionDialogStatus.getCategory());
    transaction.setDate(transactionDialogStatus.getDate());
    transaction.setName(transactionDialogStatus.getName());
    transaction.setFixCost(Boolean.valueOf(transactionDialogStatus.isFixCost()));
  }

  private void changeContinuousTransaction(final TransactionDialogStatus transactionDialogStatus) {
    if (transactionDialogStatus.getContinuousTransaction() == null) {
      throw new IllegalStateException("continuousTransaction should not be null");
    }

    if (transactionDialogStatus.getChangeFromDate() == null) {
      if (transactionDialogStatus.getRecurrenceRule() != null) {
        transactionDialogStatus
            .setDate(transactionDialogStatus.getContinuousTransaction().getDateBegin());
      }
      this.continuousTransactionProvider
          .deleteAllContinuousTransactions(transactionDialogStatus.getContinuousTransaction());
    } else {
      final List<Transaction> transactionsAfter = this.transactionProvider
          .getTransactionsFromDate(
              transactionDialogStatus.getContinuousTransaction(),
              transactionDialogStatus.getChangeFromDate());

      this.transactionProvider.deleteTransactions(transactionsAfter);

      final RecurrenceRule recurrenceRuleOfOldContinuousTransaction = RecurrenceRuleUtils
          .createRecurrenceRule(
              transactionDialogStatus.getContinuousTransaction().getRecurrenceRule());

      final LocalDate lastDateBeforeRecurrence = RecurrenceRuleUtils.getLastRecurrenceDateBefore(
          recurrenceRuleOfOldContinuousTransaction,
          transactionDialogStatus.getContinuousTransaction().getDateBegin(),
          transactionDialogStatus.getChangeFromDate());

      recurrenceRuleOfOldContinuousTransaction
          .setUntil(DateUtils.localDateToDateTime(lastDateBeforeRecurrence));
      transactionDialogStatus.getContinuousTransaction()
          .setRecurrenceRule(recurrenceRuleOfOldContinuousTransaction.toString());
      this.continuousTransactionProvider
          .merge(transactionDialogStatus.getContinuousTransaction());
    }

    if (transactionDialogStatus.getRecurrenceRule() != null) {
      this.createNewContinuousTransaction(transactionDialogStatus);
    } else {
      this.createNewTransaction(transactionDialogStatus);
    }

  }

  private void changeContinuesTransactionToTransaction(
      final TransactionDialogStatus transactionDialogStatus) {
    this.changeContinuousTransaction(transactionDialogStatus);
  }

  private void changeNewContinuousTransactionFromTransaction(
      final TransactionDialogStatus transactionDialogStatus) {
    this.transactionProvider.delete(transactionDialogStatus.getTransaction());
    this.createNewContinuousTransaction(transactionDialogStatus);

  }

  private void changeTransaction(final TransactionDialogStatus transactionDialogStatus) {

    if (transactionDialogStatus.getTransaction().getContinuousTransaction() != null) {
      this.continuousTransactionSplitterProvider
          .splitContinuousTransaction(transactionDialogStatus.getTransaction());
    } else {
      this.transactionProvider.delete(transactionDialogStatus.getTransaction());
    }

    this.createNewTransaction(transactionDialogStatus);

  }

  private void setContinuousTransactionAttributtes(
      final ContinuousTransaction continuousTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    continuousTransaction.setAmount(transactionDialogStatus.getAmount());
    continuousTransaction.setBankAccount(transactionDialogStatus.getBankaccount());
    continuousTransaction.setCategory(transactionDialogStatus.getCategory());
    continuousTransaction.setDateBegin(transactionDialogStatus.getDate());
    continuousTransaction.setName(transactionDialogStatus.getName());
    continuousTransaction.setRecurrenceRule(transactionDialogStatus.getRecurrenceRule().toString());
    continuousTransaction.setFixCost(Boolean.valueOf(transactionDialogStatus.isFixCost()));
  }

}
