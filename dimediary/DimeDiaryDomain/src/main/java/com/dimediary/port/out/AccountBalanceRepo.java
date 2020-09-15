package com.dimediary.port.out;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import java.time.LocalDate;
import java.util.List;

public interface AccountBalanceRepo {

  List<Balance> getBalanceHistories(BankAccount bankAccount);

  List<Balance> getBalanceHistoriesAfterDate(BankAccount bankAccount, LocalDate date);

  Balance getBalanceHistory(BankAccount bankAccount, LocalDate date);

  Balance getBalanceHistoryBefore(BankAccount bankAccount, LocalDate date);

  Balance getLastBalanceHistory(BankAccount bankAccount);

  void persist(Balance balance);

  void persistBalanceHistories(List<Balance> balanceHistories);

  void delete(Balance balance);

  /**
   * @param balanceHistories list of balance histories to delete
   */
  void deleteBalanceHistories(List<Balance> balanceHistories);

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  void deleteBalanceHistories(BankAccount bankAccount);

}