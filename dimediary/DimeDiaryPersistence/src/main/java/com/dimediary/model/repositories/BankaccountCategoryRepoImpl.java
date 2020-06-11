package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.converter.BankaccountCategoryTransformer;
import com.dimediary.model.repositories.helper.DatabaseTransactionProviderImpl;
import com.dimediary.port.out.BankaccountCategoryRepo;
import javax.inject.Inject;

class BankaccountCategoryRepoImpl implements BankaccountCategoryRepo {

  private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
      .getLogger(
          com.dimediary.model.repositories.BankaccountCategoryRepoImpl.class);

  @Inject
  private DatabaseTransactionProviderImpl entityManagerService;

  @Inject
  private BankaccountCategoryTransformer bankaccountCategoryTransformer;

  @Override
  public java.util.List<String> getBankAccountCategoryNames() {
    com.dimediary.model.repositories.BankaccountCategoryRepoImpl.log
        .info("getBankAccountCategoryNames");
    final java.util.ArrayList<String> names = new java.util.ArrayList<>();

    final java.util.List<com.dimediary.model.entities.BankAccountCategoryEntity> bankAccountCategories = this.entityManagerService
        .getEntityManager()
        .createNamedQuery("allAccountCategories",
            com.dimediary.model.entities.BankAccountCategoryEntity.class).getResultList();

    for (final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategory : bankAccountCategories) {
      names.add(bankAccountCategory.getName());
    }

    return names;
  }

  @Override
  public BankAccountCategory getBankAccountCategory(final String bankAccountCategoryName) {
    if (bankAccountCategoryName == null) {
      return null;
    }

    com.dimediary.model.repositories.BankaccountCategoryRepoImpl.log
        .info("getBankAccountCategory: " + bankAccountCategoryName);
    final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategory = this.entityManagerService
        .getEntityManager()
        .find(com.dimediary.model.entities.BankAccountCategoryEntity.class,
            bankAccountCategoryName);

    return this.entityToDomain(bankAccountCategory);
  }

  @Override
  public java.util.List<BankAccountCategory> getBankAccountCategories(
      final java.util.List<String> bankAccountCategoryNames) {
    if (bankAccountCategoryNames.isEmpty()) {
      return null;
    }

    for (final String string : bankAccountCategoryNames) {
      com.dimediary.model.repositories.BankaccountCategoryRepoImpl.log
          .info("getBankAccountCategory: " + string);
    }

    final java.util.List<com.dimediary.model.entities.BankAccountCategoryEntity> bankAccountCategories = this.entityManagerService
        .getEntityManager()
        .createNamedQuery("findAccountCategories",
            com.dimediary.model.entities.BankAccountCategoryEntity.class)
        .setParameter("nameList", bankAccountCategoryNames).getResultList();

    return this.entitiesToDomain(bankAccountCategories);
  }

  @Override
  public void deleteBankAccountCategories(final java.util.List<String> bankAccountCategoryNames)
      throws javax.persistence.RollbackException {
    if (bankAccountCategoryNames == null || bankAccountCategoryNames.isEmpty()) {
      return;
    }
    try {
      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final java.util.List<BankAccountCategory> bankAccountCategories = this
          .getBankAccountCategories(bankAccountCategoryNames);

      for (final BankAccountCategory bankAccountCategory : bankAccountCategories) {
        this.delete(bankAccountCategory);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
      }
    } catch (final javax.persistence.RollbackException e) {
      throw e;
    }

  }

  @Override
  public void delete(final BankAccountCategory bankAccountCategory)
      throws javax.persistence.RollbackException {
    if (bankAccountCategory == null) {
      return;
    }
    try {
      com.dimediary.model.repositories.BankaccountCategoryRepoImpl.log
          .info("delete BankAccountCategory: " + bankAccountCategory.getName());
      final boolean ownTransaction = this.entityManagerService.beginTransaction();

      final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this
          .findEntity(bankAccountCategory);

      if (bankAccountCategoryEntity != null) {
        this.entityManagerService.getEntityManager().remove(bankAccountCategoryEntity);
      }

      if (ownTransaction) {
        this.entityManagerService.commitTransaction();
      }
    } catch (final javax.persistence.RollbackException e) {
      throw new javax.persistence.RollbackException(
          "can't delete bank account category: " + bankAccountCategory.getName(), e);
    }

  }

  @Override
  public void persist(final BankAccountCategory bankAccountCategory) {
    if (bankAccountCategory == null) {
      return;
    }
    com.dimediary.model.repositories.BankaccountCategoryRepoImpl.log
        .info("persist BankAccountCategory: " + bankAccountCategory.getName());
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this
        .domainToEntity(bankAccountCategory);

    if (this.findEntity(bankAccountCategory) != null) {
      this.entityManagerService.getEntityManager().merge(bankAccountCategoryEntity);
    } else {
      this.entityManagerService.getEntityManager().persist(bankAccountCategoryEntity);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private com.dimediary.model.entities.BankAccountCategoryEntity domainToEntity(
      final BankAccountCategory bankAccountCategory) {
    return this.bankaccountCategoryTransformer
        .bankAccountCategoryToBankaccountCategoryEntity(bankAccountCategory);
  }

  private com.dimediary.model.entities.BankAccountCategoryEntity findEntity(
      final BankAccountCategory bankAccountCategory) {
    if (bankAccountCategory != null && bankAccountCategory.getName() != null
        && !bankAccountCategory.getName().isEmpty()) {
      return this.entityManagerService.getEntityManager().find(
          com.dimediary.model.entities.BankAccountCategoryEntity.class,
          bankAccountCategory.getName());
    }
    return null;
  }

  private BankAccountCategory entityToDomain(
      final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategory) {
    return this.bankaccountCategoryTransformer
        .bankAccountCategoryEntityToBankAccountCategory(bankAccountCategory);
  }

  private java.util.List<BankAccountCategory> entitiesToDomain(
      final java.util.List<com.dimediary.model.entities.BankAccountCategoryEntity> bankAccountCategories) {
    return bankAccountCategories.stream()
        .map((bankAccountCategory) -> this.entityToDomain(bankAccountCategory))
        .collect(java.util.stream.Collectors.toList());
  }
}
