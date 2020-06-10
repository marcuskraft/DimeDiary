package com.dimediary.services;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.domain.helper.DatabaseTransactionProvider;
import com.dimediary.domain.helper.TransactionDialogStatus;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.ContinuousTransactionSplitterProvider;
import com.dimediary.port.in.TransactionGenerator;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.utils.date.DateUtils;
import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import org.dmfs.rfc5545.recur.RecurrenceRule;

public class TransactionGeneratorImpl implements TransactionGenerator {

  @Inject
  private TransactionProvider transactionProvider;

  @Inject
  private ContinuousTransactionSplitterProvider continuousTransactionSplitterProvider;

  @Inject
  private ContinuousTransactionProvider continuousTransactionProvider;

  @Inject
  private DatabaseTransactionProvider entityManagerService;

  @Override
  public void generateTransactions(final TransactionDialogStatus transactionDialogStatus) {
    if (transactionDialogStatus.getTransaction() != null) {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.changeTransaction(true, transactionDialogStatus);
      } else {
        this.changeNewContinuousTransactionFromTransaction(true, transactionDialogStatus);
      }
    } else if (transactionDialogStatus.getContinuousTransaction() != null) {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.changeContinuesTransactionToTransaction(transactionDialogStatus);
      } else {
        this.changeContinuousTransaction(transactionDialogStatus);
      }
    } else {
      if (transactionDialogStatus.getRecurrenceRule() == null) {
        this.createNewTransaction(true, transactionDialogStatus);
      } else {
        this.createNewContinuousTransaction(true, transactionDialogStatus);
      }
    }
  }

  private void createNewContinuousTransaction(final boolean ownTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    if (ownTransaction) {
      this.entityManagerService.beginTransaction();
    }
    try {
      final ContinuousTransaction continuousTransaction = new ContinuousTransaction();
      this.setContinuousTransactionAttributtes(continuousTransaction, transactionDialogStatus);

      final List<Transaction> transactions = this.continuousTransactionProvider
          .generateTransactionsForContinuousTransaction(continuousTransaction);
      this.continuousTransactionProvider
          .persistContinuousTransaction(continuousTransaction, transactions);
    } catch (final Exception e) {
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private void createNewTransaction(final boolean ownTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    if (ownTransaction) {
      this.entityManagerService.beginTransaction();
    }
    try {
      final Transaction transaction = new Transaction();
      this.setTransactionAttributes(transaction, transactionDialogStatus);
      this.transactionProvider.persistTransaction(transaction);
    } catch (final Exception e) {
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
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

    this.entityManagerService.beginTransaction();
    try {
      if (transactionDialogStatus.getChangeFromDate() == null) {
        if (transactionDialogStatus.getRecurrenceRule() != null) {
          transactionDialogStatus
              .setDate(transactionDialogStatus.getContinuousTransaction().getDateBeginn());
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
            transactionDialogStatus.getContinuousTransaction().getDateBeginn(),
            transactionDialogStatus.getChangeFromDate());

        recurrenceRuleOfOldContinuousTransaction
            .setUntil(DateUtils.localDateToDateTime(lastDateBeforeRecurrence));
        transactionDialogStatus.getContinuousTransaction()
            .setRecurrenceRule(recurrenceRuleOfOldContinuousTransaction.toString());
        this.continuousTransactionProvider
            .merge(transactionDialogStatus.getContinuousTransaction());
      }

      if (transactionDialogStatus.getRecurrenceRule() != null) {
        this.createNewContinuousTransaction(false, transactionDialogStatus);
      } else {
        this.createNewTransaction(false, transactionDialogStatus);
      }
    } catch (final Exception e) {
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
    this.entityManagerService.commitTransaction();
  }

  private void changeContinuesTransactionToTransaction(
      final TransactionDialogStatus transactionDialogStatus) {
    this.changeContinuousTransaction(transactionDialogStatus);
  }

  private void changeNewContinuousTransactionFromTransaction(final boolean ownTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    if (ownTransaction) {
      this.entityManagerService.beginTransaction();
    }
    try {
      this.transactionProvider.delete(transactionDialogStatus.getTransaction());
      this.createNewContinuousTransaction(false, transactionDialogStatus);
    } catch (final Exception e) {
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private void changeTransaction(final boolean ownTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    if (ownTransaction) {
      this.entityManagerService.beginTransaction();
    }
    try {
      if (transactionDialogStatus.getTransaction().getContinuousTransaction() != null) {
        this.continuousTransactionSplitterProvider
            .splitContinuousTransaction(transactionDialogStatus.getTransaction());
      } else {
        this.transactionProvider.delete(transactionDialogStatus.getTransaction());
      }

      this.createNewTransaction(false, transactionDialogStatus);
    } catch (final Exception e) {
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private void setContinuousTransactionAttributtes(
      final ContinuousTransaction continuousTransaction,
      final TransactionDialogStatus transactionDialogStatus) {
    continuousTransaction.setAmount(transactionDialogStatus.getAmount());
    continuousTransaction.setBankAccount(transactionDialogStatus.getBankaccount());
    continuousTransaction.setCategory(transactionDialogStatus.getCategory());
    continuousTransaction.setDateBeginn(transactionDialogStatus.getDate());
    continuousTransaction.setName(transactionDialogStatus.getName());
    continuousTransaction.setRecurrenceRule(transactionDialogStatus.getRecurrenceRule().toString());
    continuousTransaction.setFixCost(Boolean.valueOf(transactionDialogStatus.isFixCost()));
  }

}
