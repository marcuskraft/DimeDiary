package com.dimediary.port.out;

import com.dimediary.domain.BankAccount;
import java.util.List;
import java.util.UUID;

public interface BankAccountRepo {

  List<BankAccount> getBankAccount();

  BankAccount getBankAccount(UUID bankAccountId);

  void delete(BankAccount bankAccount);

  BankAccount persist(BankAccount bankAccount);


}