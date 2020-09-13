package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import java.util.List;

public interface BankAccountProvider {

  List<String> getBankAccountNames();

  BankAccount getBankAccount(String bankAccountName);

  void deleteBankAccount(String bankAccountName);

  BankAccount persist(BankAccount bankAccount);

  void update(BankAccount bankAccount, String bankAccountName);
}
