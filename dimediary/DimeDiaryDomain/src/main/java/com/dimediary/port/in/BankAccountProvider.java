package com.dimediary.port.in;

import com.dimediary.domain.BankAccount;
import java.util.List;

public interface BankAccountProvider {

  List<String> getBankAccountNames();

  BankAccount getBankAccount(String bankaccountName);

  void deleteBankAccounts(List<String> bankAccountNames);

  void persist(BankAccount bankAccount);
}
