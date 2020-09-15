package com.dimediary.services;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Transaction;
import com.dimediary.port.in.BalanceUseCase;
import com.dimediary.port.out.AccountBalanceRepo;
import com.dimediary.port.out.BankAccountRepo;
import com.dimediary.port.out.TransactionRepo;
import com.dimediary.utils.date.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService implements BalanceUseCase {


  private final TransactionRepo transactionService;
  private final AccountBalanceRepo accountBalanceRepo;
  private final BankAccountRepo bankAccountRepo;


  @Autowired
  public BalanceService(final TransactionRepo transactionService,
      final AccountBalanceRepo accountBalanceRepo,
      final BankAccountRepo bankAccountRepo) {
    this.transactionService = transactionService;
    this.accountBalanceRepo = accountBalanceRepo;
    this.bankAccountRepo = bankAccountRepo;
  }

  @Override
  public List<Balance> getBalancesFollowingDays(final UUID bankAccountId,
      final LocalDate dateFrom,
      final LocalDate dateUntil) {
    final BankAccount bankAccount = this.bankAccountRepo.getBankAccount(bankAccountId);
    final List<LocalDate> dates = DateUtils.getLocalDatesFromTo(dateFrom, dateUntil);

    return this.getBalancesFollowingDays(bankAccount, dates);
  }


  /**
   * updates the balance history after adding or deleting a transaction if the transaction belongs
   * to a bank account
   *
   * @param transaction the transaction for the updates; only this transaction will be considered
   *                    for update
   * @param action      defines whether the transaction is added or deleted
   */
  public void updateBalance(final Transaction transaction, final BalanceAction action) {
    BalanceService.log
        .info("updateBalance for transaction {} and action {}", transaction, action.name());
    if (transaction.getBankAccount() == null) {
      return;
    }

    final Balance lastBalance = this.accountBalanceRepo
        .getLastBalanceHistory(transaction.getBankAccount());

    this.updateBalance(transaction, action, lastBalance);
  }

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  public void deleteBalanceHistories(final BankAccount bankAccount) {
    if (bankAccount == null) {
      return;
    }
    this.accountBalanceRepo.deleteBalanceHistories(bankAccount);
  }


  private List<Balance> getBalancesFollowingDays(final BankAccount bankAccount,
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

  private Balance getBalance(final BankAccount bankAccount, final LocalDate date) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBalance())) {
      return null;
    }

    final Balance lastBalanceBeforeRequestedDate = this
        .getLastBalanceHistory(bankAccount, date,
            LocalDate.now());

    if (lastBalanceBeforeRequestedDate == null) {
      return this.getBalanceWithAllTransactions(bankAccount, date);
    }

    final Integer result = this
        .sumAllTransactionsBetween(bankAccount, date, lastBalanceBeforeRequestedDate);

    final Balance balance = new Balance();
    balance.setBankAccount(bankAccount);
    balance.setDate(date);
    balance.setBalance(result);

    return balance;

  }

  private void persistBalanceHistories(final List<Balance> balanceHistories) {
    this.accountBalanceRepo.persistBalanceHistories(balanceHistories);
  }

  private Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date) {
    return this.accountBalanceRepo.getBalanceHistoryBefore(bankAccount, date);
  }

  private Balance getLastBalanceHistory(final BankAccount bankAccount) {
    BalanceService.log.info("getLastBalanceHistory for bankaccount {}", bankAccount.getName());
    return this.accountBalanceRepo.getLastBalanceHistory(bankAccount);
  }

  private Balance getLastBalanceHistory(final BankAccount bankAccount, final LocalDate date,
      final LocalDate today) {
    BalanceService.log
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
    this.generateBalances(bankAccount, bankAccount.getStartBalanceEuroCent(),
        bankAccount.getDateStartBalance(), today);


  }

  private void checkBalanceHistories(final BankAccount bankAccount, final LocalDate today) {
    final Balance lastBalance = this.getLastBalanceHistory(bankAccount);

    if (lastBalance == null) {
      this.generateMissingBalanceHistories(bankAccount, null, today);
      return;
    }

    final LocalDate dateOfLastBalance = lastBalance.getDate();

    if (!dateOfLastBalance.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      BalanceService.log.error(
          "Balancehistories should only be on Sundays but was {} for bankaccount {} and date {}",
          dateOfLastBalance.getDayOfWeek(), lastBalance.getBankAccount().getName(),
          lastBalance.getDate());
      throw new IllegalStateException("Balancehistories should only be on Sundays");
    }

    final LocalDate lastSundayBeforeToday = DateUtils.getLastSunday(today);

    if (dateOfLastBalance.isBefore(lastSundayBeforeToday)) {
      this.generateMissingBalanceHistories(bankAccount, lastBalance, today);
    }

  }

  /**
   * proofs the balance history of this bank account. Initialize the balance history if no exists.
   * Corrects wrong entries in the balance history if there are some.
   *
   * @param bankAccount bank account to proof
   * @param lastBalance last balance
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

  private void generateBalances(final BankAccount bankAccount, Integer lastAmount,
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
      final Integer balanceDayBefore) {
    if (bankAccount == null || date == null || date.isBefore(bankAccount.getDateStartBalance())) {
      return null;
    }

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount, date);

    Integer result;
    if (balanceDayBefore == null) {
      result = this.getBalance(bankAccount, date.minusDays(1)).getBalance();
      if (result == null) {
        result = 0;
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
    balance.setBalance(result);

    return balance;
  }

  private Balance getBalanceWithAllTransactions(final BankAccount bankAccount,
      final LocalDate date) {
    Integer amount;

    amount = bankAccount.getStartBalanceEuroCent();

    final List<Transaction> transactions = this.transactionService
        .getTransactions(bankAccount.getDateStartBalance(),
            date, bankAccount);

    for (final Transaction transaction : transactions) {
      amount += transaction.getAmount();
    }

    final Balance balance = new Balance();
    balance.setBankAccount(bankAccount);
    balance.setDate(date);
    balance.setBalance(amount);

    return balance;
  }

  private Integer sumAllTransactionsBetween(final BankAccount bankAccount, final LocalDate date,
      final Balance balance) {
    Integer result = balance.getBalance();
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

    final List<Balance> balanceHistories = this.accountBalanceRepo
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

    this.accountBalanceRepo.persistBalanceHistories(balanceHistories);
  }

}
