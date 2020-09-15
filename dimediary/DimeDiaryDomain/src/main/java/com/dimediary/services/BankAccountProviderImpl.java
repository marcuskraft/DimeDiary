package com.dimediary.services;

import com.dimediary.domain.BankAccount;
import com.dimediary.port.in.BankAccountProvider;
import com.dimediary.port.out.BankAccountRepo;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankAccountProviderImpl implements BankAccountProvider {


  private final BankAccountRepo bankAccountRepo;


  private final BalanceService balanceService;

  @Autowired
  public BankAccountProviderImpl(final BankAccountRepo bankAccountRepo,
      final BalanceService balanceService) {
    this.bankAccountRepo = bankAccountRepo;
    this.balanceService = balanceService;
  }

  @Override
  public List<String> getBankAccountNames() {
    List<String> bankAccountNames = null;
    try {
      bankAccountNames = this.bankAccountRepo.getBankAccountNames();
    } catch (final Exception e) {
      this.log.error("error during load of bank account names", e);
    }
    return bankAccountNames;
  }

  @Override
  public BankAccount getBankAccount(final UUID bankAccountId) {
    BankAccount bankAccount = null;
    try {
      bankAccount = this.bankAccountRepo.getBankAccount(bankAccountId);
    } catch (final Exception e) {
      this.log.error("error during load of bank account", e);
    }
    return bankAccount;
  }

  @Override
  public void deleteBankAccount(final UUID bankAccountId) {
    final BankAccount bankAccount = this.bankAccountRepo
        .getBankAccount(bankAccountId);
    this.balanceService.deleteBalanceHistories(bankAccount);
    this.bankAccountRepo.delete(bankAccount);
  }

  @Override
  public BankAccount persist(final BankAccount bankAccount) {
    return this.bankAccountRepo.persist(bankAccount);
  }

  @Override
  public void update(final BankAccount bankAccount, final String bankAccountName) {

  }

}
