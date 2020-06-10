package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.BankAccountCategory;
import java.util.List;

public interface BankaccountRepo {

  List<String> getBankAccountNames();

  BankAccount getBankAccount(String bankAccountName);

  List<BankAccount> getBankAccounts(List<String> bankAccountsNames);

  void delete(BankAccount bankAccount);

  List<BankAccount> getBankAccounts(BankAccountCategory bankAccountCategory);

  void persist(BankAccount bankAccount);

}