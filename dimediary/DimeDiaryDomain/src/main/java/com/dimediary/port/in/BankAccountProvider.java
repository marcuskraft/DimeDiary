package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import java.util.List;
import java.util.UUID;

public interface BankAccountProvider {

  List<String> getBankAccountNames();

  BankAccount getBankAccount(UUID bankAccountId);

  void deleteBankAccount(UUID bankAccountId);

  BankAccount persist(BankAccount bankAccount);

  void update(BankAccount bankAccount, String bankAccountName);
}
