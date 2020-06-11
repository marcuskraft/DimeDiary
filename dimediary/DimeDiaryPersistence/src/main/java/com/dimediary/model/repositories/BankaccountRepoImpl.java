package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.converter.BankaccountTransformer;
import com.dimediary.model.repositories.helper.DatabaseTransactionProviderImpl;
import com.dimediary.port.out.BankaccountRepo;
import javax.inject.Inject;
import org.apache.logging.log4j.Logger;

class BankaccountRepoImpl implements BankaccountRepo {

  @Inject
  private Logger log;

  @Inject
  private DatabaseTransactionProviderImpl entityManagerService;

  @Inject
  private BankaccountTransformer bankaccountTransformer;

  @Override
  public java.util.List<String> getBankAccountNames() {
    this.log.info("getBankAccountNames");
    final java.util.List<String> names = new java.util.ArrayList<>();

    final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts = this.entityManagerService
        .getEntityManager()
        .createNamedQuery(
            com.dimediary.model.entities.BankAccountEntity.ALL_BANK_ACCOUNTS,
            com.dimediary.model.entities.BankAccountEntity.class).getResultList();

    for (final com.dimediary.model.entities.BankAccountEntity bankAccount : bankAccounts) {
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
    final com.dimediary.model.entities.BankAccountEntity bankAccount = this.entityManagerService
        .getEntityManager().find(
            com.dimediary.model.entities.BankAccountEntity.class,
            bankAccountName);

    return this.bankaccountTransformer.bankAccountEntityToBankaccount(bankAccount);
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

    final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts = this.entityManagerService
        .getEntityManager()
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
      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
          .findEntity(bankAccount);

      if (bankAccountEntity != null) {
        this.entityManagerService.getEntityManager().remove(bankAccountEntity);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
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

    final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this.entityManagerService
        .getEntityManager()
        .find(com.dimediary.model.entities.BankAccountCategoryEntity.class,
            bankAccountCategory.getName());

    final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts = this.entityManagerService
        .getEntityManager()
        .createNamedQuery(
            com.dimediary.model.entities.BankAccountEntity.FIND_BANKACCOUNTS_WITH_CATEGORY,
            com.dimediary.model.entities.BankAccountEntity.class)
        .setParameter("bankAccountCategory", bankAccountCategoryEntity).getResultList();

    return this.entitiesToDomains(bankAccounts);
  }

  @Override
  public void persist(final BankAccount bankAccount) {
    if (bankAccount == null) {
      return;
    }
    this.log.info("persist BankAccount: " + bankAccount.getName());
    try {
      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this.bankaccountTransformer
          .bankAccountToBankAccountEntity(bankAccount);

      if (this.findEntity(bankAccount) == null) {
        this.entityManagerService.getEntityManager().persist(bankAccountEntity);
      } else {
        this.entityManagerService.getEntityManager().merge(bankAccountEntity);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
      }
    } catch (final Exception e) {
      this.log.error("can't persist bankaccount", e);
      this.entityManagerService.rollbackTransaction();
      throw e;
    }
  }

  private java.util.List<BankAccount> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.BankAccountEntity> bankAccounts) {
    return bankAccounts.stream()
        .map((bankAccount) -> this.bankaccountTransformer
            .bankAccountEntityToBankaccount(bankAccount))
        .collect(java.util.stream.Collectors.toList());
  }

  private com.dimediary.model.entities.BankAccountEntity findEntity(final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null) {
      return this.entityManagerService.getEntityManager().find(
          com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
    }
    return null;
  }

}
