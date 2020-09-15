package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.BankAccountCategory;
import java.util.List;
import java.util.UUID;

public interface BankAccountRepo {

  List<String> getBankAccountNames();

  BankAccount getBankAccount(UUID bankAccountId);

  List<BankAccount> getBankAccounts(List<String> bankAccountsNames);

  void delete(BankAccount bankAccount);

  List<BankAccount> getBankAccounts(BankAccountCategory bankAccountCategory);

  BankAccount persist(BankAccount bankAccount);


}