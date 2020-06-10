package com.dimediary.services;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Transaction;
import com.dimediary.domain.helper.AmountUtils;
import com.dimediary.domain.helper.DatabaseTransactionProvider;
import com.dimediary.port.in.AccountBalanceProvider;
import com.dimediary.port.out.AccountBalanceRepo;
import com.dimediary.port.out.TransactionRepo;
import com.dimediary.utils.date.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

public class AccountBalanceProviderImpl implements AccountBalanceProvider {

  @Inject
  private Logger log;

  @Inject
  private TransactionRepo transactionService;

  @Inject
  private AccountBalanceRepo accountBalanceService;

  @Inject
  private DatabaseTransactionProvider databaseTransactionProvider;

  /**
   * @param bankAccount the bank account
   * @param date        the date
   * @return returns the balance for this bank account and date
   */
  @Override
  public Double getBalance(final BankAccount bankAccount, final LocalDate date) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBudget())) {
      return null;
    }

    final BalanceHistory lastBalanceHistoryBeforeRequestedDate = this
        .getLastBalanceHistory(bankAccount, date,
            LocalDate.now());

    if (lastBalanceHistoryBeforeRequestedDate == null) {
      return this.getBalanceWithAllTransactions(bankAccount, date);
    }

    final Double result = this
        .sumAllTransactionsBetween(bankAccount, date, lastBalanceHistoryBeforeRequestedDate);

    return AmountUtils.round(result);

  }

  /**
   * @param bankAccount bank account for which the balances are requested
   * @param dates       sequence of following days for which the balances are requested
   * @return returns a HashMap with the given Dates and the corresponding balances for this bank
   * account on this date
   */
  @Override
  public Map<LocalDate, Double> getBalancesFollowingDays(final BankAccount bankAccount,
      final List<LocalDate> dates) {
    final HashMap<LocalDate, Double> balances = new HashMap<>();

    Double lastBalance = 0.0;

    dates.sort(null);

    for (int i = 0; i < dates.size(); i++) {
      if (i == 0) {
        lastBalance = this.getBalance(bankAccount, dates.get(i));
      } else {
        lastBalance = this.getBalance(bankAccount, dates.get(i), lastBalance);
      }
      balances.put(dates.get(i), lastBalance);
    }

    return balances;
  }

  /**
   * updates the balance history after adding or deleting a transaction if the transaction belongs
   * to a bank account
   *
   * @param transaction the transaction for the updates; only this transaction will be considered
   *                    for update
   * @param action      defines whether the transaction is added or deleted
   */
  @Override
  public void updateBalance(final Transaction transaction, final BalanceAction action) {
    this.log.info("updateBalance for transaction {} and action {}", transaction, action.name());
    if (transaction.getBankAccount() == null) {
      return;
    }

    final BalanceHistory lastBalanceHistory = this.accountBalanceService
        .getLastBalanceHistory(transaction.getBankAccount());

    this.updateBalance(transaction, action, lastBalanceHistory);
  }

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  @Override
  public void deleteBalanceHistories(final BankAccount bankAccount) {
    if (bankAccount == null) {
      return;
    }
    this.accountBalanceService.deleteBalanceHistories(bankAccount);
  }

  @Override
  public void persistBalanceHistories(final List<BalanceHistory> balanceHistories) {
    this.accountBalanceService.persistBalanceHistories(balanceHistories);
  }

  @Override
  public BalanceHistory getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date) {
    return this.accountBalanceService.getBalanceHistoryBefore(bankAccount, date);
  }

  @Override
  public BalanceHistory getLastBalanceHistory(final BankAccount bankAccount) {
    this.log.info("getLastBalanceHistory for bankaccount {}", bankAccount.getName());
    return this.accountBalanceService.getLastBalanceHistory(bankAccount);
  }

  @Override
  public BalanceHistory getLastBalanceHistory(final BankAccount bankAccount, final LocalDate date,
      final LocalDate today) {
    this.log
        .info("getLastBalanceHistory for bankaccount {} and date {}", bankAccount.getName(), date);

    this.checkBalanceHistories(bankAccount, today);
    return this.getBalanceHistoryBefore(bankAccount, date);
  }

  /**
   * initialize the balance history for this bank account. All old balance histories will be
   * deleted.
   *
   * @param bankAccount bank account to initialize
   */
  private void initBalance(final BankAccount bankAccount, final LocalDate today) {
    try {
      this.deleteBalanceHistories(bankAccount);
      this.generateBalances(bankAccount, bankAccount.getStartBudget(),
          bankAccount.getDateStartBudget(), today);
    } catch (final Exception e) {
      this.databaseTransactionProvider.rollbackTransaction();
      throw e;
    }

  }

  private boolean checkBalanceHistories(final BankAccount bankAccount, final LocalDate today) {
    final BalanceHistory lastBalanceHistory = this.getLastBalanceHistory(bankAccount);

    if (lastBalanceHistory == null) {
      this.generateMissingBalanceHistories(bankAccount, lastBalanceHistory, today);
      return true;
    }

    final LocalDate dateOfLastBalance = lastBalanceHistory.getDate();

    if (!dateOfLastBalance.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      this.log.error(
          "Balancehistories should only be on Sundays but was {} for bankaccount {} and date {}",
          dateOfLastBalance.getDayOfWeek(), lastBalanceHistory.getBankAccount().getName(),
          lastBalanceHistory.getDate());
      throw new IllegalStateException("Balancehistories should only be on Sundays");
    }

    final LocalDate lastSundayBeforeToday = DateUtils.getLastSunday(today);

    if (dateOfLastBalance.isBefore(lastSundayBeforeToday)) {
      this.generateMissingBalanceHistories(bankAccount, lastBalanceHistory, today);
      return true;
    }

    return false;
  }

  /**
   * proofs the balance history of this bank account. Initialize the balance history if no exists.
   * Corrects wrong entries in the balance history if there are some.
   *
   * @param bankAccount        bank account to proof
   * @param lastBalanceHistory
   * @throws Exception
   */
  private void generateMissingBalanceHistories(final BankAccount bankAccount,
      final BalanceHistory lastBalanceHistory,
      final LocalDate today) {
    final boolean ownTransaction = this.databaseTransactionProvider.beginTransaction();

    try {
      if (lastBalanceHistory == null) {
        this.initBalance(bankAccount, today);
        return;
      }

      final LocalDate dateForNextBalanceHistory = DateUtils
          .getNextSundayAlways(lastBalanceHistory.getDate());
      this.generateBalances(bankAccount, lastBalanceHistory.getAmount(), dateForNextBalanceHistory,
          today);
    } catch (final Exception e) {
      this.databaseTransactionProvider.rollbackTransaction();
      throw e;
    }

    if (ownTransaction) {
      this.databaseTransactionProvider.commitTransaction();
    }
  }

  private void generateBalances(final BankAccount bankAccount, Double lastAmount,
      final LocalDate dateForNextBalanceHistory, final LocalDate today) {
    final List<LocalDate> sundays = DateUtils
        .getAllSundayForBalancing(dateForNextBalanceHistory, today);

    LocalDate lastSunday;
    final ArrayList<BalanceHistory> balanceHistories = new ArrayList<>();
    for (final LocalDate sunday : sundays) {
      final BalanceHistory balanceHistory = new BalanceHistory();

      lastSunday = DateUtils.getLastSundayAlways(sunday);
      final List<Transaction> transactions = this.transactionService
          .getTransactions(lastSunday.plusDays(1),
              sunday, bankAccount);

      for (final Transaction transaction : transactions) {
        lastAmount += transaction.getAmount();
      }

      balanceHistory.setBankAccount(bankAccount);
      balanceHistory.setDate(sunday);
      balanceHistory.setAmount(lastAmount);

      balanceHistories.add(balanceHistory);
    }

    this.persistBalanceHistories(balanceHistories);
  }

  private Double getBalance(final BankAccount bankAccount, final LocalDate date,
      final Double balanceDayBefore) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBudget())) {
      return null;
    }

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount, date);

    Double result;
    if (balanceDayBefore == null) {
      result = this.getBalance(bankAccount, date.minusDays(1));
      if (result == null) {
        result = 0.0;
      }
    } else {
      result = balanceDayBefore;
    }

    for (final Transaction transaction : transactions) {
      result += transaction.getAmount();
    }

    return AmountUtils.round(result);
  }

  private Double getBalanceWithAllTransactions(final BankAccount bankAccount,
      final LocalDate date) {
    Double amount;

    amount = bankAccount.getStartBudget();

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount.getDateStartBudget(),
            date, bankAccount);

    for (final Transaction transaction : transactions) {
      amount += transaction.getAmount();
    }

    return AmountUtils.round(amount);
  }

  private Double sumAllTransactionsBetween(final BankAccount bankAccount, final LocalDate date,
      final BalanceHistory balanceHistory) {
    Double result = balanceHistory.getAmount();
    final LocalDate dateFrom = balanceHistory.getDate().plusDays(1);

    final List<Transaction> transactions = this.transactionService
        .getTransactions(dateFrom, date, bankAccount);

    for (final Transaction transaction : transactions) {
      result += transaction.getAmount();
    }
    return result;
  }

  private void updateBalance(final Transaction transaction, final BalanceAction action,
      final BalanceHistory lastBalanceHistory) {
    if (lastBalanceHistory == null || transaction.getDate().isAfter(lastBalanceHistory.getDate())) {
      return;
    }

    final List<BalanceHistory> balanceHistories = this.accountBalanceService
        .getBalanceHistoriesAfterDate(transaction.getBankAccount(), transaction.getDate());

    switch (action) {
      case adding:
        for (final BalanceHistory balanceHistory : balanceHistories) {
          balanceHistory.addAmount(transaction.getAmount());
        }
        break;
      case deleting:
        for (final BalanceHistory balanceHistory : balanceHistories) {
          balanceHistory.addAmount(-transaction.getAmount());
        }
        break;
      default:
        break;

    }

    this.accountBalanceService.persistBalanceHistories(balanceHistories);
  }

}
