package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.converter.ContinuousTransactionTransformer;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import com.dimediary.model.entities.RecurrenceExceptionEntity;
import com.dimediary.model.entities.RecurrenceExtraInstanceEntity;
import com.dimediary.model.repositories.cruds.ContinuousTransactionCrudRepository;
import com.dimediary.model.repositories.cruds.RecurrenceExceptionCrudRepository;
import com.dimediary.model.repositories.cruds.RecurrenceExtraInstanceCrudReporitory;
import com.dimediary.port.out.ContinuousTransactionRepo;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class ContinuousTransactionRepoImpl implements ContinuousTransactionRepo {

  private final ContinuousTransactionTransformer continuousTransactionTransformer;
  private final ContinuousTransactionCrudRepository continuousTransactionCrudRepository;
  private final RecurrenceExceptionCrudRepository recurrenceExceptionCrudRepository;
  private final RecurrenceExtraInstanceCrudReporitory recurrenceExtraInstanceCrudReporitory;

  @Autowired
  public ContinuousTransactionRepoImpl(
      final ContinuousTransactionTransformer continuousTransactionTransformer,
      final ContinuousTransactionCrudRepository continuousTransactionCrudRepository,
      final RecurrenceExceptionCrudRepository recurrenceExceptionCrudRepository,
      final RecurrenceExtraInstanceCrudReporitory recurrenceExtraInstanceCrudReporitory) {
    this.continuousTransactionTransformer = continuousTransactionTransformer;
    this.continuousTransactionCrudRepository = continuousTransactionCrudRepository;
    this.recurrenceExceptionCrudRepository = recurrenceExceptionCrudRepository;
    this.recurrenceExtraInstanceCrudReporitory = recurrenceExtraInstanceCrudReporitory;
  }


  @Override
  public List<ContinuousTransaction> getContinuousTransactions(
      final BankAccount bankAccount) {
    Validate.notNull(bankAccount);

    ContinuousTransactionRepoImpl.log
        .info("geContinuousTransactions for: " + bankAccount.getName());

    return this.continuousTransactionCrudRepository
        .getAllByBankAccountId(bankAccount.getId().toString())
        .stream().map(
            this.continuousTransactionTransformer::continuousTransactionEntityToContinuousTransaction)
        .collect(Collectors.toList());
  }


  @Override
  public ContinuousTransaction persist(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);

    ContinuousTransactionRepoImpl.log
        .info("persist ContinuousTransaction: " + continuousTransaction.getId());

    if (continuousTransaction.getId() == null) {
      continuousTransaction.setId(UUID.randomUUID());
    }

    ContinuousTransactionEntity continuousTransactionEntity = this
        .domainToEntity(continuousTransaction);

    continuousTransactionEntity = this.continuousTransactionCrudRepository
        .save(continuousTransactionEntity);

    return this.continuousTransactionTransformer
        .continuousTransactionEntityToContinuousTransaction(continuousTransactionEntity);
  }

  @Override
  public void delete(final ContinuousTransaction continuousTransaction) {
    Validate.notNull(continuousTransaction);
    Validate.notNull(continuousTransaction.getId());

    com.dimediary.model.repositories.ContinuousTransactionRepoImpl.log
        .info("deleteAllContinuousTransactions : " + continuousTransaction.getId());

    this.continuousTransactionCrudRepository.deleteById(continuousTransaction.getId().toString());

  }

  @Override
  public ContinuousTransaction getContinuousTransaction(final UUID continuousTransactionId) {
    return this.continuousTransactionTransformer
        .continuousTransactionEntityToContinuousTransaction(this.continuousTransactionCrudRepository
            .findById(continuousTransactionId.toString()).orElseThrow(() -> new NoResultException(
                "no continuousTransaction found with id " + continuousTransactionId)));


  }

  private com.dimediary.model.entities.ContinuousTransactionEntity domainToEntity(
      final ContinuousTransaction continuousTransaction) {

    // generate the RecurrenceExceptionEntity to save
    final Collection<RecurrenceExceptionEntity> exceptionsToSave = continuousTransaction
        .getRecurrenceSettings().getRecurrenceExceptions().stream().map(localDate -> {
          final RecurrenceExceptionEntity recurrenceExceptionEntity = new RecurrenceExceptionEntity();
          recurrenceExceptionEntity.setExceptionDate(localDate);
          return recurrenceExceptionEntity;
        }).collect(Collectors.toList());

    // generate the RecurrenceExtraInstanceEntity to save
    final Collection<RecurrenceExtraInstanceEntity> extraInstancesToSave = continuousTransaction
        .getRecurrenceSettings().getRecurrenceExtraInstances().stream().map(localDate -> {
          final RecurrenceExtraInstanceEntity recurrenceExtraInstanceEntity = new RecurrenceExtraInstanceEntity();
          recurrenceExtraInstanceEntity.setInstanceDate(localDate);
          return recurrenceExtraInstanceEntity;
        }).collect(Collectors.toList());

    // if there are already some exceptions or extra instances than set the id from them so that they are not saved twice
    if (continuousTransaction.getId() != null) {
      final Collection<RecurrenceExceptionEntity> exceptions = this.recurrenceExceptionCrudRepository
          .getAllByContinuousTransaction_Id(continuousTransaction.getId().toString());
      final Collection<RecurrenceExtraInstanceEntity> extraInstances = this.recurrenceExtraInstanceCrudReporitory
          .getAllByContinuousTransaction_Id(continuousTransaction.getId().toString());

      exceptionsToSave.forEach(recurrenceExceptionEntity -> {
        exceptions.forEach(recurrenceExceptionEntityOld -> {
          if (recurrenceExceptionEntity.getContinuousTransaction().getId()
              .equals(recurrenceExceptionEntityOld.getContinuousTransaction().getId())
              && recurrenceExceptionEntity.getExceptionDate()
              .equals(recurrenceExceptionEntityOld.getExceptionDate())) {
            recurrenceExceptionEntity.setId(recurrenceExceptionEntityOld.getId());
          }
        });
        if (recurrenceExceptionEntity.getId() == null) {
          recurrenceExceptionEntity.setId(UUID.randomUUID().toString());
        }
      });

      extraInstancesToSave.forEach(recurrenceExtraInstanceEntity -> {
        extraInstances.forEach(recurrenceExtraInstanceEntityOld -> {
          if (recurrenceExtraInstanceEntity.getContinuousTransaction().getId()
              .equals(recurrenceExtraInstanceEntityOld.getContinuousTransaction().getId())
              && recurrenceExtraInstanceEntity.getInstanceDate()
              .equals(recurrenceExtraInstanceEntityOld.getInstanceDate())) {
            recurrenceExtraInstanceEntity.setId(recurrenceExtraInstanceEntityOld.getId());
          }
        });
        if (recurrenceExtraInstanceEntity.getId() == null) {
          recurrenceExtraInstanceEntity.setId(UUID.randomUUID().toString());
        }
      });

    }

    // transform the continuous transaction
    final ContinuousTransactionEntity continuousTransactionEntity = this.continuousTransactionTransformer
        .continuousTransactionToContinuousTransactionEntity(continuousTransaction,
            exceptionsToSave,
            extraInstancesToSave);

    // set the continuous transaction for the exceptions and extra instances
    continuousTransactionEntity.getExceptions().forEach(
        recurrenceExceptionEntity -> recurrenceExceptionEntity
            .setContinuousTransaction(continuousTransactionEntity));
    continuousTransactionEntity.getExtraInstances().forEach(
        recurrenceExtraInstanceEntity -> recurrenceExtraInstanceEntity
            .setContinuousTransaction(continuousTransactionEntity));

    return continuousTransactionEntity;
  }

}
