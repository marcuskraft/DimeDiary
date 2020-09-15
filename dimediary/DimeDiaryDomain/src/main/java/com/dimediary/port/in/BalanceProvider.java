package com.dimediary.port.in;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface BalanceProvider {


  enum BalanceAction {
    adding, deleting
  }

  List<Balance> getBalancesFollowingDays(String bankAccountName, LocalDate dateFrom,
      LocalDate dateUntil);

  void updateBalance(final Transaction transaction, final BalanceAction action);

  void deleteBalanceHistories(final BankAccount bankAccount);

  void persistBalanceHistories(final List<Balance> balanceHistories);

  Balance getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date);

  Balance getLastBalanceHistory(final BankAccount bankAccount);

  Balance getLastBalanceHistory(final BankAccount bankAccount, final LocalDate date,
      final LocalDate today);

}
