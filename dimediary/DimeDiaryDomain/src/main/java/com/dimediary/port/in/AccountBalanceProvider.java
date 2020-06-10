package com.dimediary.port.in;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.domain.BankAccount;
import com.dimediary.domain.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AccountBalanceProvider {

  enum BalanceAction {
    adding, deleting
  }

  Double getBalance(final BankAccount bankAccount, final LocalDate date);

  Map<LocalDate, Double> getBalancesFollowingDays(final BankAccount bankAccount,
      final List<LocalDate> dates);

  void updateBalance(final Transaction transaction, final BalanceAction action);

  void deleteBalanceHistories(final BankAccount bankAccount);

  void persistBalanceHistories(final List<BalanceHistory> balanceHistories);

  BalanceHistory getBalanceHistoryBefore(final BankAccount bankAccount,
      final LocalDate date);

  BalanceHistory getLastBalanceHistory(final BankAccount bankAccount);

  BalanceHistory getLastBalanceHistory(final BankAccount bankAccount, final LocalDate date,
      final LocalDate today);

}
