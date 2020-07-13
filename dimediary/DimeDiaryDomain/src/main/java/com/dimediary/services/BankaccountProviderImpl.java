package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.port.in.AccountBalanceProvider;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.port.out.BankaccountRepo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankaccountProviderImpl implements BankAccountProvider {


  private final BankaccountRepo bankaccountService;


  private final AccountBalanceProvider accountBalanceProvider;

  @Autowired
  public BankaccountProviderImpl(final BankaccountRepo bankaccountService,
      final AccountBalanceProvider accountBalanceProvider) {
    this.bankaccountService = bankaccountService;
    this.accountBalanceProvider = accountBalanceProvider;
  }

  @Override
  public List<String> getBankAccountNames() {
    List<String> bankAccountNames = null;
    try {
      bankAccountNames = this.bankaccountService.getBankAccountNames();
    } catch (final Exception e) {
      this.log.error("error during load of bank account names", e);
    }
    return bankAccountNames;
  }

  @Override
  public BankAccount getBankAccount(final String bankaccountName) {
    BankAccount bankAccount = null;
    try {
      bankAccount = this.bankaccountService.getBankAccount(bankaccountName);
    } catch (final Exception e) {
      this.log.error("error during load of bank account", e);
    }
    return bankAccount;
  }

  @Override
  public void deleteBankAccounts(final List<String> bankAccountNames) {
    final List<BankAccount> bankAccounts = this.bankaccountService
        .getBankAccounts(bankAccountNames);

    for (final BankAccount bankAccount : bankAccounts) {
      this.accountBalanceProvider.deleteBalanceHistories(bankAccount);
      this.bankaccountService.delete(bankAccount);
    }

  }

  @Override
  public void persist(final BankAccount bankAccount) {
    this.bankaccountService.persist(bankAccount);
  }

}
