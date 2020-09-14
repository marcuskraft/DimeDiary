package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.model.converter.BankaccountCategoryTransformer;
import com.dimediary.model.entities.BankAccountCategoryEntity;
import com.dimediary.port.out.BankAccountCategoryRepo;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class BankAccountCategoryRepoImpl implements BankAccountCategoryRepo {


  @PersistenceContext
  private final EntityManager entityManager;

  private final BankaccountCategoryTransformer bankaccountCategoryTransformer;

  @Autowired
  public BankAccountCategoryRepoImpl(final EntityManager entityManager,
      final BankaccountCategoryTransformer bankaccountCategoryTransformer) {
    this.entityManager = entityManager;
    this.bankaccountCategoryTransformer = bankaccountCategoryTransformer;
  }


  @Override
  public java.util.List<String> getBankAccountCategoryNames() {
    com.dimediary.model.repositories.BankAccountCategoryRepoImpl.log
        .info("getBankAccountCategoryNames");
    final java.util.ArrayList<String> names = new java.util.ArrayList<>();

    final java.util.List<com.dimediary.model.entities.BankAccountCategoryEntity> bankAccountCategories = this.entityManager
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

    com.dimediary.model.repositories.BankAccountCategoryRepoImpl.log
        .info("getBankAccountCategory: " + bankAccountCategoryName);
    final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategory = this.entityManager
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
      com.dimediary.model.repositories.BankAccountCategoryRepoImpl.log
          .info("getBankAccountCategory: " + string);
    }

    final java.util.List<com.dimediary.model.entities.BankAccountCategoryEntity> bankAccountCategories = this.entityManager
        .createNamedQuery("findAccountCategories",
            com.dimediary.model.entities.BankAccountCategoryEntity.class)
        .setParameter("nameList", bankAccountCategoryNames).getResultList();

    return this.entitiesToDomain(bankAccountCategories);
  }

  @Override
  public List<BankAccountCategory> getBankAccountCategories() {
    final List<BankAccountCategoryEntity> bankAccountCategoryEntities = this.entityManager
        .createNamedQuery(BankAccountCategoryEntity.ALL_ACCOUNT_CATEGORIES,
            BankAccountCategoryEntity.class).getResultList();

    return bankAccountCategoryEntities.stream()
        .map(this.bankaccountCategoryTransformer::bankAccountCategoryEntityToBankAccountCategory)
        .collect(
            Collectors.toList());
  }

  @Override
  public void deleteBankAccountCategories(final java.util.List<String> bankAccountCategoryNames)
      throws javax.persistence.RollbackException {
    if (bankAccountCategoryNames == null || bankAccountCategoryNames.isEmpty()) {
      return;
    }
    try {
      final java.util.List<BankAccountCategory> bankAccountCategories = this
          .getBankAccountCategories(bankAccountCategoryNames);

      for (final BankAccountCategory bankAccountCategory : bankAccountCategories) {
        this.delete(bankAccountCategory);
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
      com.dimediary.model.repositories.BankAccountCategoryRepoImpl.log
          .info("delete BankAccountCategory: " + bankAccountCategory.getName());

      final com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this
          .findEntity(bankAccountCategory);

      if (bankAccountCategoryEntity != null) {
        this.entityManager.remove(bankAccountCategoryEntity);
      }

    } catch (final javax.persistence.RollbackException e) {
      throw new javax.persistence.RollbackException(
          "can't delete bank account category: " + bankAccountCategory.getName(), e);
    }

  }

  @Override
  public BankAccountCategory persist(final BankAccountCategory bankAccountCategory) {
    if (bankAccountCategory == null) {
      return null;
    }
    com.dimediary.model.repositories.BankAccountCategoryRepoImpl.log
        .info("persist BankAccountCategory: " + bankAccountCategory.getName());

    com.dimediary.model.entities.BankAccountCategoryEntity bankAccountCategoryEntity = this
        .domainToEntity(bankAccountCategory);

    if (this.findEntity(bankAccountCategory) != null) {
      bankAccountCategoryEntity = this.entityManager.merge(bankAccountCategoryEntity);
    } else {
      this.entityManager.persist(bankAccountCategoryEntity);
    }

    return this.bankaccountCategoryTransformer
        .bankAccountCategoryEntityToBankAccountCategory(bankAccountCategoryEntity);

  }

  @Override
  public void delete(final String bankAccountCategoryName) {
    final BankAccountCategoryEntity bankAccountCategoryEntity = this.entityManager
        .find(BankAccountCategoryEntity.class, bankAccountCategoryName);
    if (bankAccountCategoryEntity != null) {
      this.entityManager.remove(bankAccountCategoryEntity);
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
      return this.entityManager.find(
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
