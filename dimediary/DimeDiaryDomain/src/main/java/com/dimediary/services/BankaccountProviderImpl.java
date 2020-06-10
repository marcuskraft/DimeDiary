package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.port.in.AccountBalanceProvider;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.port.out.BankaccountRepo;
import java.util.List;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

public class BankaccountProviderImpl implements BankAccountProvider {

  @Inject
  private Logger logger;

  @Inject
  private BankaccountRepo bankaccountService;

  @Inject
  private AccountBalanceProvider accountBalanceProvider;

  @Override
  public List<String> getBankAccountNames() {
    List<String> bankAccountNames = null;
    try {
      bankAccountNames = this.bankaccountService.getBankAccountNames();
    } catch (final Exception e) {
      this.logger.error("error during load of bank account names", e);
    }
    return bankAccountNames;
  }

  @Override
  public BankAccount getBankAccount(final String bankaccountName) {
    BankAccount bankAccount = null;
    try {
      bankAccount = this.bankaccountService.getBankAccount(bankaccountName);
    } catch (final Exception e) {
      this.logger.error("error during load of bank account", e);
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
