package com.dimediary.port.out;

import com.dimediary.domain.Balance;
import com.dimediary.domain.BankAccount;
import java.time.LocalDate;
import java.util.List;

public interface AccountBalanceRepo {


  List<Balance> getBalanceHistoriesAfterDate(BankAccount bankAccount, LocalDate date);

  Balance getBalanceHistoryBefore(BankAccount bankAccount, LocalDate date);

  Balance getLastBalanceHistory(BankAccount bankAccount);

  void persistBalanceHistories(List<Balance> balanceHistories);

  void deleteBalanceHistories(BankAccount bankAccount);

}