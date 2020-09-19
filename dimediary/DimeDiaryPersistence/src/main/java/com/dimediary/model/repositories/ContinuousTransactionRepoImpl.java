package com.dimediary.model.repositories;

import com.dimediary.domain.BankAccount;
import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.converter.ContinuousTransactionTransformer;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import com.dimediary.model.repositories.cruds.ContinuousTransactionCrudRepository;
import com.dimediary.port.out.ContinuousTransactionRepo;
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

  @Autowired
  public ContinuousTransactionRepoImpl(
      final ContinuousTransactionTransformer continuousTransactionTransformer,
      final ContinuousTransactionCrudRepository continuousTransactionCrudRepository) {
    this.continuousTransactionTransformer = continuousTransactionTransformer;
    this.continuousTransactionCrudRepository = continuousTransactionCrudRepository;
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
    return this.continuousTransactionTransformer
        .continuousTransactionToContinuousTransactionEntity(continuousTransaction);
  }

}
