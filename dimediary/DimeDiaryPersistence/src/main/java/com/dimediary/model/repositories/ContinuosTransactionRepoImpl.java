package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.converter.ContinuousTransactionTransformer;
import com.dimediary.port.out.ContinuosTransactionRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class ContinuosTransactionRepoImpl implements ContinuosTransactionRepo {

  @PersistenceContext
  private final EntityManager entityManager;

  private final ContinuousTransactionTransformer continuousTransactionTransformer;

  @Autowired
  public ContinuosTransactionRepoImpl(final EntityManager entityManager,
      final ContinuousTransactionTransformer continuousTransactionTransformer) {
    this.entityManager = entityManager;
    this.continuousTransactionTransformer = continuousTransactionTransformer;
  }


  /**
   * @param bankAccount
   * @return all ContinuousTransactions belonging to this account
   */
  @Override
  public java.util.List<ContinuousTransaction> getContinuousTransactions(
      final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    com.dimediary.model.repositories.ContinuosTransactionRepoImpl.log
        .info("geContinuousTransactions for: " + bankAccount.getName());

    final com.dimediary.model.entities.BankAccountEntity bankAccountEntity = this
        .findBankAccountEntity(bankAccount);

    final java.util.List<com.dimediary.model.entities.ContinuousTransactionEntity> continuousTransactionEntities = this.entityManager
        .createNamedQuery(
            com.dimediary.model.entities.ContinuousTransactionEntity.CONTINUOUS_TRANSACTION_FOR_BANK_ACCOUNT,
            com.dimediary.model.entities.ContinuousTransactionEntity.class)
        .setParameter("bankAccount", bankAccountEntity).getResultList();

    return this.entitiesToDomains(continuousTransactionEntities);
  }

  /**
   * persists the given continuous transaction
   *
   * @param continuousTransaction
   */
  @Override
  public void persist(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    com.dimediary.model.repositories.ContinuosTransactionRepoImpl.log
        .info("persist ContinuousTransaction: " + continuousTransaction.getId());

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .domainToEntity(continuousTransaction);

    if (this.findEntity(continuousTransaction) != null) {
      this.entityManager.merge(continuousTransactionEntity);
    } else {
      this.entityManager.persist(continuousTransactionEntity);
      this.entityManager.refresh(continuousTransactionEntity);
    }

    continuousTransaction.setId(continuousTransactionEntity.getId());


  }

  @Override
  public void delete(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    com.dimediary.model.repositories.ContinuosTransactionRepoImpl.log
        .info("deleteAllContinuousTransactions : " + continuousTransaction.getId());

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .findEntity(continuousTransaction);

    if (continuousTransactionEntity != null) {
      this.entityManager.remove(continuousTransactionEntity);
    }

  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccountEntity(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.BankAccountEntity.class, bankAccount.getName());
    }
    return null;
  }

  private java.util.List<ContinuousTransaction> entitiesToDomains(
      final java.util.List<com.dimediary.model.entities.ContinuousTransactionEntity> continuousTransactionEntities) {
    return continuousTransactionEntities.stream()
        .map((continuousTransactionEntity) -> this.entityToDomain(continuousTransactionEntity))
        .collect(java.util.stream.Collectors.toList());
  }

  private ContinuousTransaction entityToDomain(
      final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity) {
    return this.continuousTransactionTransformer
        .continuousTransactionEntityToContinuousTransaction(continuousTransactionEntity);
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity findEntity(
      final ContinuousTransaction continuousTransaction) {
    if (continuousTransaction != null && continuousTransaction.getId() != null) {
      return this.entityManager.find(
          com.dimediary.model.entities.ContinuousTransactionEntity.class,
          continuousTransaction.getId());
    }
    return null;
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity domainToEntity(
      final ContinuousTransaction continuousTransaction) {
    return this.continuousTransactionTransformer
        .continuousTransactionToContinuousTransactionEntity(continuousTransaction);
  }

}
