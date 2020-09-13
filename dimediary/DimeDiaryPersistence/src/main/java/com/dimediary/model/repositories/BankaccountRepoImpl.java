package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.converter.BankaccountTransformer;
import com.dimediary.model.entities.BankAccountEntity;
import com.dimediary.model.repositories.cruds.BankAccountCrudRepository;
import com.dimediary.port.out.BankaccountRepo;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class BankaccountRepoImpl implements BankaccountRepo {


  @PersistenceContext
  private final EntityManager entityManager;

  private final BankaccountTransformer bankaccountTransformer;

  private final BankAccountCrudRepository bankAccountCrudRepository;

  @Autowired
  public BankaccountRepoImpl(final EntityManager entityManager,
      final BankaccountTransformer bankaccountTransformer,
      final BankAccountCrudRepository bankAccountCrudRepository) {
    this.entityManager = entityManager;
    this.bankaccountTransformer = bankaccountTransformer;
    this.bankAccountCrudRepository = bankAccountCrudRepository;
  }


  @Override
  public java.util.List<String> getBankAccountNames() {
    this.log.info("getBankAccountNames");
    final java.util.List<String> names = new java.util.ArrayList<>();

    final Iterable<BankAccountEntity> ban = this.bankAccountCrudRepository.findAll();

    for (final com.dimediary.model.entities.BankAccountEntity bankAccount : ban) {
      names.add(bankAccount.getName());
    }

    return names;
  }

  @Override
  public BankAccount getBankAccount(final String bankAccountName) {
    if (bankAccountName == null) {
      return null;
    }
    this.log.info("getBankAccount: " + bankAccountName);
    final com.dimediary.model.entities.BankAccountEntity bankAccount = this.entityManager.find(
        com.dimediary.model.entities.BankAccountEntity.class,
        bankAccountName);

    return this.bankaccountTransformer.bankAccountEntityToBankAccount(bankAccount);
  }

  @Override
  public BankAccount getBankAccount(final UUID bankAccountId) {
    return null;
  }

  @Override
  public java.util.List<BankAccount> getBankAccounts(
      final java.util.List<String> bankAccountsNames) {

    if (bankAccountsNames.isEmpty()) {
      return null;
    }

    for (final String string : bankAccountsNames) {
      this.log.info("getBankAccounts: " + string);
    }

    final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.BankAccountEntity.FIND_BANK_ACCOUNTS,
            com.dimediary.model.entities.BankAccountEntity.class)
        .setParameter("namesList", bankAccountsNames).getResultList();
    return this.entitiesToDomains(bankAccounts);
  }

  @Override
  public void delete(final BankAccount bankAccount) throws javax.persistence.RollbackException {
    if (bankAccount == null) {
      return;
    }
    try {
      this.log.info("delete BankAccount: " + bankAccount.getName());

      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findEntity(bankAccount);

      if (bankAccountEntity != null) {
        this.entityManager.remove(bankAccountEntity);
      }

    } catch (final javax.persistence.RollbackException e) {
      throw new javax.persistence.RollbackException(
          "can't delete bank account: " + bankAccount.getName(), e);
    }
  }

  @Override
  public java.util.List<BankAccount> getBankAccounts(
      final BankAccountCategory bankAccountCategory) {
    if (bankAccountCategory == null) {
      return null;
    }
    this.log.info("getBankAccounts by Category: " + bankAccountCategory);

    final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this.entityManager
        .find(com.dimediary.model.entities.BankAccountCategoryEntity.class,
            bankAccountCategory.getName());

    final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.BankAccountEntity.FIND_BANKACCOUNTS_WITH_CATEGORY,
            com.dimediary.model.entities.BankAccountEntity.class)
        .setParameter("bankAccountCategory", bankAccountCategoryEntity).getResultList();

    return this.entitiesToDomains(bankAccounts);
  }

  @Override
  public BankAccount persist(final BankAccount bankAccount) {
    if (bankAccount == null) {
      return null;
    }
    BankaccountRepoImpl.log.info("persist BankAccount: " + bankAccount.getName());
    try {

      com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this.bankaccountTransformer
          .bankAccountToBankAccountEntity(bankAccount);

      if (this.findEntity(bankAccount) == null) {
        this.entityManager.persist(bankAccountEntity);
      } else {
        bankAccountEntity = this.entityManager.merge(bankAccountEntity);
      }

      return this.bankaccountTransformer.bankAccountEntityToBankAccount(bankAccountEntity);

    } catch (final Exception e) {
      BankaccountRepoImpl.log.error("can't persist bankaccount", e);
      throw e;
    }
  }

  private java.util.List<BankAccount> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts) {
    return bankAccounts.stream()
        .map((bankAccount) -> this.bankaccountTransformer
            .bankAccountEntityToBankAccount(bankAccount))
        .collect(java.util.stream.Collectors.toList());
  }

  private com.dimediary.model.entities.BankAccountEntity findEntity(final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
    }
    return null;
  }

}
