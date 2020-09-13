package com.dimediary.services;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.port.in.ContinuousTransactionSplitterProvider;
import com.dimediary.port.in.TransactionProvider;
import com.dimediary.utils.date.DateUtils;
import com.dimediary.utils.recurrence.RecurrenceRuleUtils;
import java.time.LocalDate;
import java.util.List;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContinuousTransactionSplitterProviderImpl implements
    ContinuousTransactionSplitterProvider {

  private final TransactionProvider transactionProvider;

  private final ContinuousTransactionProvider continuousTransactionProvider;

  @Autowired
  public ContinuousTransactionSplitterProviderImpl(
      final TransactionProvider transactionProvider,
      final ContinuousTransactionProvider continuousTransactionProvider) {
    this.transactionProvider = transactionProvider;
    this.continuousTransactionProvider = continuousTransactionProvider;
  }

  /**
   * splits the continuous transaction around the given transaction. Two new continuous transactions
   * are created and the old one with all his transactions will be deleted. The given transaction
   * will be deleted and there will be no new transaction at this date whether in the continuous
   * transaction before nor in the the one after this transaction. The recurrence rule itself is
   * still the same for both new continuous transactions.
   *
   * @param transaction
   */
  @Override
  public void splitContinuousTransaction(final Transaction transaction) {
    this.splitContinuousTransaction(transaction.getContinuousTransaction(),
        transaction.getDate().minusDays(1),
        transaction.getDate().plusDays(1));
  }

  /**
   * splits the continuous transaction into two continuous transactions. The first one will start at
   * the same date as the old continuous transaction and will end not later than lastDateBefore. The
   * second one will start not earlier than firstDateAfter and end at the same date as the old one.
   * The old continuous transaction with all his transactions will be deleted. The recurrence rule
   * itself is still the same for both new continuous transactions.
   *
   * @param continuousTransaction
   * @param lastDateBefore
   * @param firstDateAfter
   */
  private void splitContinuousTransaction(final ContinuousTransaction continuousTransaction,
      final LocalDate lastDateBefore, final LocalDate firstDateAfter) {
    // TODO: don't delete all transactions, only reorganize the continuous
    // transaction into two

    final RecurrenceRule recurrenceRuleOriginal = RecurrenceRuleUtils
        .createRecurrenceRule(continuousTransaction.getRecurrenceRule());

    final List<Transaction> transactionsBefore = this
        .generateContinuousTransactionBefore(continuousTransaction,
            lastDateBefore);

    this.generateContinuousTransactionAfter(continuousTransaction, firstDateAfter,
        recurrenceRuleOriginal,
        transactionsBefore);

    this.continuousTransactionProvider.deleteAllContinuousTransactions(continuousTransaction);


  }

  private void generateContinuousTransactionAfter(final ContinuousTransaction continuousTransaction,
      final LocalDate firstDateAfter, final RecurrenceRule recurrenceRuleOriginal,
      final List<Transaction> transactionsBefore) {
    final ContinuousTransaction continuousTransactionAfter = continuousTransaction.getCopy();

    final RecurrenceRule recurrenceRuleAfter = RecurrenceRuleUtils
        .createRecurrenceRule(continuousTransactionAfter.getRecurrenceRule());

    final LocalDate firstDateAfterRecurrence = RecurrenceRuleUtils
        .getFirstRecurrenceDateAfter(recurrenceRuleAfter,
            continuousTransactionAfter.getDateBegin(), firstDateAfter.minusDays(1));

    continuousTransactionAfter.setDateBegin(firstDateAfterRecurrence);

    boolean continuousTransactionsAfterIsNeeded = true;
    if (recurrenceRuleOriginal.getCount() != null) {
      final int numberOfTransactionsBefore =
          transactionsBefore != null ? transactionsBefore.size() : 0;

      final int numberOfTransactionsAfter =
          recurrenceRuleOriginal.getCount() - numberOfTransactionsBefore - 1;
      if (numberOfTransactionsAfter > 0) {
        recurrenceRuleAfter.setCount(numberOfTransactionsAfter);
      } else {
        continuousTransactionsAfterIsNeeded = false;
      }
    }

    if (continuousTransactionsAfterIsNeeded) {
      continuousTransactionAfter.setRecurrenceRule(recurrenceRuleAfter.toString());
      final List<Transaction> transactionsAfter = this.transactionProvider
          .getTransactionsFromDate(continuousTransaction, firstDateAfterRecurrence);
      if (transactionsAfter == null) {
        return;
      }

      for (final Transaction transaction : transactionsAfter) {
        transaction.setContinuousTransaction(continuousTransactionAfter);
      }

      this.persistContinuousTransactionMergeTransactions(continuousTransactionAfter,
          transactionsAfter);
    }
  }

  private List<Transaction> generateContinuousTransactionBefore(
      final ContinuousTransaction continuousTransaction,
      final LocalDate lastDateBefore) {
    final ContinuousTransaction continuousTransactionBefore = continuousTransaction.getCopy();

    final RecurrenceRule recurrenceRuleBefore = RecurrenceRuleUtils
        .createRecurrenceRule(continuousTransactionBefore.getRecurrenceRule());

    final LocalDate lastDateBeforeRecurrence = RecurrenceRuleUtils
        .getLastRecurrenceDateBefore(recurrenceRuleBefore,
            continuousTransactionBefore.getDateBegin(), lastDateBefore.plusDays(1));

    if (lastDateBeforeRecurrence == null) {
      return null;
    }

    recurrenceRuleBefore.setUntil(DateUtils.localDateToDateTime(lastDateBeforeRecurrence));

    continuousTransactionBefore.setRecurrenceRule(recurrenceRuleBefore.toString());

    final List<Transaction> transactionsBefore = this.transactionProvider
        .getTransactionsUntil(continuousTransaction, lastDateBeforeRecurrence);

    if ((transactionsBefore != null) && !transactionsBefore.isEmpty()) {
      for (final Transaction transaction : transactionsBefore) {
        transaction.setContinuousTransaction(continuousTransactionBefore);
      }
      this.persistContinuousTransactionMergeTransactions(continuousTransactionBefore,
          transactionsBefore);
    }
    return transactionsBefore;
  }

  private void persistContinuousTransactionMergeTransactions(
      final ContinuousTransaction continuousTransaction,
      final List<Transaction> transactions) {
    if ((continuousTransaction == null) || (transactions == null) || transactions.isEmpty()) {
      return;
    }

    this.continuousTransactionProvider.persist(continuousTransaction);
    this.transactionProvider.persistTransactions(transactions);

  }

}
