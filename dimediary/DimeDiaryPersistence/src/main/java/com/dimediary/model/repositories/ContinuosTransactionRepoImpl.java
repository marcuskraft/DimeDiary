package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.converter.ContinuosTransactionTransformer;
import com.dimediary.model.repositories.helper.DatabaseTransactionProviderImpl;
import com.dimediary.port.out.ContinuosTransactionRepo;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;

class ContinuosTransactionRepoImpl implements ContinuosTransactionRepo {

  private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
      .getLogger(
          com.dimediary.model.repositories.ContinuosTransactionRepoImpl.class);

  @Inject
  private DatabaseTransactionProviderImpl entityManagerService;

  @Inject
  private ContinuosTransactionTransformer continuosTransactionTransformer;

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

    final java.util.List<com.dimediary.model.entities.ContinuousTransactionEntity> continuousTransactionEntities = this.entityManagerService
        .getEntityManager()
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

    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .domainToEntity(continuousTransaction);

    if (this.findEntity(continuousTransaction) != null) {
      this.entityManagerService.getEntityManager().merge(continuousTransactionEntity);
    } else {
      this.entityManagerService.getEntityManager().persist(continuousTransactionEntity);
      this.entityManagerService.getEntityManager().refresh(continuousTransactionEntity);
    }

    continuousTransaction.setId(continuousTransactionEntity.getId());

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }

  }

  @Override
  public void delete(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    com.dimediary.model.repositories.ContinuosTransactionRepoImpl.log
        .info("deleteAllContinuousTransactions : " + continuousTransaction.getId());
    final boolean ownTransaction = this.entityManagerService.beginTransaction();

    final com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity = this
        .findEntity(continuousTransaction);

    if (continuousTransactionEntity != null) {
      this.entityManagerService.getEntityManager().remove(continuousTransactionEntity);
    }

    if (ownTransaction) {
      this.entityManagerService.commitTransaction();
    }
  }

  private com.dimediary.model.entities.BankAccountEntity findBankAccountEntity(
      final BankAccount bankAccount) {
    if (bankAccount != null && bankAccount.getName() != null) {
      return this.entityManagerService.getEntityManager().find(
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
    return this.continuosTransactionTransformer
        .continuousTransactionEntityToContinuousTransaction(continuousTransactionEntity);
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity findEntity(
      final ContinuousTransaction continuousTransaction) {
    if (continuousTransaction != null && continuousTransaction.getId() != null) {
      return this.entityManagerService.getEntityManager().find(
          com.dimediary.model.entities.ContinuousTransactionEntity.class,
          continuousTransaction.getId());
    }
    return null;
  }

  private com.dimediary.model.entities.ContinuousTransactionEntity domainToEntity(
      final ContinuousTransaction continuousTransaction) {
    return this.continuosTransactionTransformer
        .continuousTransactionToContinuousTransactionEntity(continuousTransaction);
  }

}
