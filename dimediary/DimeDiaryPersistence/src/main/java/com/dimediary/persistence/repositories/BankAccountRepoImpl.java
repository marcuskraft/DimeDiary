package com.dimediary.persistence.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.persistence.converter.BankAccountTransformer;
import com.dimediary.persistence.entities.BankAccountEntity;
import com.dimediary.persistence.repositories.cruds.BankAccountCrudRepository;
import com.dimediary.persistence.repositories.utils.RepoUtils;
import com.dimediary.port.out.BankAccountRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class BankAccountRepoImpl implements BankAccountRepo {


  private final BankAccountTransformer bankaccountTransformer;

  private final BankAccountCrudRepository bankAccountCrudRepository;

  @Autowired
  public BankAccountRepoImpl(final BankAccountTransformer bankaccountTransformer,
      final BankAccountCrudRepository bankAccountCrudRepository) {
    this.bankaccountTransformer = bankaccountTransformer;
    this.bankAccountCrudRepository = bankAccountCrudRepository;
  }


  @Override
  public List<BankAccount> getBankAccount() {
    BankAccountRepoImpl.log.info("getBankAccountNames");
    final List<BankAccount> bankAccounts = new ArrayList<>();
    this.bankAccountCrudRepository.findAll()
        .forEach(bankAccountEntity -> bankAccounts
            .add(this.bankaccountTransformer.bankAccountEntityToBankAccount(bankAccountEntity)));
    return bankAccounts;
  }

  @Override
  public BankAccount getBankAccount(final UUID bankAccountId) {
    if (bankAccountId == null) {
      return null;
    }
    BankAccountRepoImpl.log.info("getBankAccount: " + bankAccountId);
    return this.bankaccountTransformer
        .bankAccountEntityToBankAccount(this.bankAccountCrudRepository
            .findById(bankAccountId.toString())
            .orElseThrow(() -> RepoUtils.getNoResultException("bank account", bankAccountId)));
  }


  @Override
  public void delete(final BankAccount bankAccount) throws RollbackException {
    if (bankAccount == null) {
      return;
    }
    BankAccountRepoImpl.log.info("delete BankAccount: {}", bankAccount.getName());
    this.bankAccountCrudRepository.deleteById(bankAccount.getId().toString());
  }

  @Override
  public BankAccount persist(final BankAccount bankAccount) {
    if (bankAccount == null) {
      return null;
    }
    BankAccountRepoImpl.log.info("persist BankAccount: " + bankAccount.getName());

    if (bankAccount.getId() == null) {
      bankAccount.setId(UUID.randomUUID());
    }

    BankAccountEntity bankAccountEntity = this.bankaccountTransformer
        .bankAccountToBankAccountEntity(bankAccount);

    bankAccountEntity = this.bankAccountCrudRepository.save(bankAccountEntity);

    return this.bankaccountTransformer.bankAccountEntityToBankAccount(bankAccountEntity);
  }

}
