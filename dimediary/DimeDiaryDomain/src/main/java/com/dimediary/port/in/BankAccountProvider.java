package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import java.util.List;
import java.util.UUID;

public interface BankAccountProvider {

  List<BankAccount> getBankAccounts();

  BankAccount getBankAccount(UUID bankAccountId);

  void deleteBankAccount(UUID bankAccountId);

  BankAccount persist(BankAccount bankAccount);

}
