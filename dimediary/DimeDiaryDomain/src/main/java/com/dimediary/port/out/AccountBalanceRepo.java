package com.dimediary.port.out;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.domain.BankAccount;
import java.time.LocalDate;
import java.util.List;

public interface AccountBalanceRepo {

  List<BalanceHistory> getBalanceHistories(BankAccount bankAccount);

  List<BalanceHistory> getBalanceHistoriesAfterDate(BankAccount bankAccount, LocalDate date);

  BalanceHistory getBalanceHistory(BankAccount bankAccount, LocalDate date);

  BalanceHistory getBalanceHistoryBefore(BankAccount bankAccount, LocalDate date);

  BalanceHistory getLastBalanceHistory(BankAccount bankAccount);

  void persist(BalanceHistory balanceHistory);

  void persistBalanceHistories(List<BalanceHistory> balanceHistories);

  void delete(BalanceHistory balanceHistory);

  /**
   * @param balanceHistories list of balance histories to delete
   */
  void deleteBalanceHistories(List<BalanceHistory> balanceHistories);

  /**
   * @param bankAccount bank account for which all balance histories will be deleted
   */
  void deleteBalanceHistories(BankAccount bankAccount);

}