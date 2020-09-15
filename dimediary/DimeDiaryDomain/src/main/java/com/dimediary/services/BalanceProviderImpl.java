package com.dimediary.services;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Transaction;
import com.dimediary.domain.helper.AmountUtils;
import com.dimediary.port.in.BalanceProvider;
import com.dimediary.port.out.AccountBalanceRepo;
import com.dimediary.port.out.BankAccountRepo;
import com.dimediary.port.out.TransactionRepo;
import com.dimediary.utils.date.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceProviderImpl implements BalanceProvider {


  private final TransactionRepo transactionService;
  private final AccountBalanceRepo accountBalanceService;
  private final BankAccountRepo bankAccountRepo;


  @Autowired
  public BalanceProviderImpl(final TransactionRepo transactionService,
      final AccountBalanceRepo accountBalanceService,
      final BankAccountRepo bankAccountRepo) {
    this.transactionService = transactionService;
    this.accountBalanceService = accountBalanceService;
    this.bankAccountRepo = bankAccountRepo;
  }

  @Override
  public List<Balance> getBalancesFollowingDays(final String bankAccountName,
      final LocalDate dateFrom,
      final LocalDate dateUntil) {
    final BankAccount bankAccount = this.bankAccountRepo.getBankAccount(bankAccountName);
    final List<LocalDate> dates = DateUtils.getLocalDatesFromTo(dateFrom, dateUntil);

    return this.getBalancesFollowingDays(bankAccount, dates);
  }


  public Balance getBalance(final BankAccount bankAccount, final LocalDate date) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBalance())) {
      return null;
    }

    final Balance lastBalanceBeforeRequestedDate = this
        .getLastBalanceHistory(bankAccount, date,
            LocalDate.now());

    if (lastBalanceBeforeRequestedDate == null) {
      return this.getBalanceWithAllTransactions(bankAccount, date);
    }

    final Double result = this
        .sumAllTransactionsBetween(bankAccount, date, lastBalanceBeforeRequestedDate);

    final Balance balance = new Balance();
    balance.setBankAccount(bankAccount);
    balance.setDate(date);
    balance.setBalance(AmountUtils.round(AmountUtils.round(result)));

    return balance;

  }


  public List<Balance> getBalancesFollowingDays(final BankAccount bankAccount,
      final List<LocalDate> dates) {
    final List<Balance> balances = new ArrayList<>();

    Balance lastBalance = null;

    dates.sort(LocalDate::compareTo);

    for (int i = 0; i < dates.size(); i++) {
      if (i == 0) {
        lastBalance = this.getBalance(bankAccount, dates.get(i));
      } else {
        lastBalance = this.getBalance(bankAccount, dates.get(i), lastBalance.getBalance());
      }
      balances.add(lastBalance);
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

    final Balance lastBalance = this.accountBalanceService
        .getLastBalanceHistory(transaction.getBankAccount());

    this.updateBalance(transaction, action, lastBalance);
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
  public void persistBalanceHistories(final List<Balance> balanceHistories) {
    this.accountBalanceService.persistBalanceHistories(balanceHistories);
  }

  @Override
  public Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date) {
    return this.accountBalanceService.getBalanceHistoryBefore(bankAccount, date);
  }

  @Override
  public Balance getLastBalanceHistory(final BankAccount bankAccount) {
    this.log.info("getLastBalanceHistory for bankaccount {}", bankAccount.getName());
    return this.accountBalanceService.getLastBalanceHistory(bankAccount);
  }

  @Override
  public Balance getLastBalanceHistory(final BankAccount bankAccount, final LocalDate date,
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
    this.deleteBalanceHistories(bankAccount);
    this.generateBalances(bankAccount, bankAccount.getStartBalance(),
        bankAccount.getDateStartBalance(), today);


  }

  private boolean checkBalanceHistories(final BankAccount bankAccount, final LocalDate today) {
    final Balance lastBalance = this.getLastBalanceHistory(bankAccount);

    if (lastBalance == null) {
      this.generateMissingBalanceHistories(bankAccount, lastBalance, today);
      return true;
    }

    final LocalDate dateOfLastBalance = lastBalance.getDate();

    if (!dateOfLastBalance.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      this.log.error(
          "Balancehistories should only be on Sundays but was {} for bankaccount {} and date {}",
          dateOfLastBalance.getDayOfWeek(), lastBalance.getBankAccount().getName(),
          lastBalance.getDate());
      throw new IllegalStateException("Balancehistories should only be on Sundays");
    }

    final LocalDate lastSundayBeforeToday = DateUtils.getLastSunday(today);

    if (dateOfLastBalance.isBefore(lastSundayBeforeToday)) {
      this.generateMissingBalanceHistories(bankAccount, lastBalance, today);
      return true;
    }

    return false;
  }

  /**
   * proofs the balance history of this bank account. Initialize the balance history if no exists.
   * Corrects wrong entries in the balance history if there are some.
   *
   * @param bankAccount bank account to proof
   * @param lastBalance
   * @throws Exception
   */
  private void generateMissingBalanceHistories(final BankAccount bankAccount,
      final Balance lastBalance,
      final LocalDate today) {

    if (lastBalance == null) {
      this.initBalance(bankAccount, today);
      return;
    }

    final LocalDate dateForNextBalanceHistory = DateUtils
        .getNextSundayAlways(lastBalance.getDate());
    this.generateBalances(bankAccount, lastBalance.getBalance(), dateForNextBalanceHistory,
        today);


  }

  private void generateBalances(final BankAccount bankAccount, Double lastAmount,
      final LocalDate dateForNextBalanceHistory, final LocalDate today) {
    final List<LocalDate> sundays = DateUtils
        .getAllSundayForBalancing(dateForNextBalanceHistory, today);

    LocalDate lastSunday;
    final ArrayList<Balance> balanceHistories = new ArrayList<>();
    for (final LocalDate sunday : sundays) {
      final Balance balance = new Balance();

      lastSunday = DateUtils.getLastSundayAlways(sunday);
      final List<Transaction> transactions = this.transactionService
          .getTransactions(lastSunday.plusDays(1),
              sunday, bankAccount);

      for (final Transaction transaction : transactions) {
        lastAmount += transaction.getAmount();
      }

      balance.setBankAccount(bankAccount);
      balance.setDate(sunday);
      balance.setBalance(lastAmount);

      balanceHistories.add(balance);
    }

    this.persistBalanceHistories(balanceHistories);
  }

  private Balance getBalance(final BankAccount bankAccount, final LocalDate date,
      final Double balanceDayBefore) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBalance())) {
      return null;
    }

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount, date);

    Double result;
    if (balanceDayBefore == null) {
      result = this.getBalance(bankAccount, date.minusDays(1)).getBalance();
      if (result == null) {
        result = 0.0;
      }
    } else {
      result = balanceDayBefore;
    }

    for (final Transaction transaction : transactions) {
      result += transaction.getAmount();
    }

    final Balance balance = new Balance();
    balance.setBankAccount(bankAccount);
    balance.setDate(date);
    balance.setBalance(AmountUtils.round(AmountUtils.round(result)));

    return balance;
  }

  private Balance getBalanceWithAllTransactions(final BankAccount bankAccount,
      final LocalDate date) {
    Double amount;

    amount = bankAccount.getStartBalance();

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount.getDateStartBalance(),
            date, bankAccount);

    for (final Transaction transaction : transactions) {
      amount += transaction.getAmount();
    }

    final Balance balance = new Balance();
    balance.setBankAccount(bankAccount);
    balance.setDate(date);
    balance.setBalance(AmountUtils.round(amount));

    return balance;
  }

  private Double sumAllTransactionsBetween(final BankAccount bankAccount, final LocalDate date,
      final Balance balance) {
    Double result = balance.getBalance();
    final LocalDate dateFrom = balance.getDate().plusDays(1);

    final List<Transaction> transactions = this.transactionService
        .getTransactions(dateFrom, date, bankAccount);

    for (final Transaction transaction : transactions) {
      result += transaction.getAmount();
    }
    return result;
  }

  private void updateBalance(final Transaction transaction, final BalanceAction action,
      final Balance lastBalance) {
    if (lastBalance == null || transaction.getDate().isAfter(lastBalance.getDate())) {
      return;
    }

    final List<Balance> balanceHistories = this.accountBalanceService
        .getBalanceHistoriesAfterDate(transaction.getBankAccount(), transaction.getDate());

    switch (action) {
      case adding:
        for (final Balance balance : balanceHistories) {
          balance.addAmount(transaction.getAmount());
        }
        break;
      case deleting:
        for (final Balance balance : balanceHistories) {
          balance.addAmount(-transaction.getAmount());
        }
        break;
      default:
        break;

    }

    this.accountBalanceService.persistBalanceHistories(balanceHistories);
  }

}
