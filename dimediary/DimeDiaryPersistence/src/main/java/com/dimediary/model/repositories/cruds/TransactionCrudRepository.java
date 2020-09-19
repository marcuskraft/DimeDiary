package com.dimediary.model.repositories.cruds;

import com.dimediary.model.entities.TransactionEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TransactionCrudRepository extends CrudRepository<TransactionEntity, String> {

  List<TransactionEntity> getAllByBankAccountIdAndDateIsGreaterThanEqualAndDateIsLessThanEqual(
      String bankAccountId, LocalDate dateFrom, LocalDate dateUntil);

  List<TransactionEntity> getAllByBankAccountIdAndDateOrderByTimestamp(String bankAccountId,
      LocalDate date);

  List<TransactionEntity> getAllByContinuousTransactionIdAndDateIsGreaterThanEqual(
      String continuousTransactionId, LocalDate dateFrom);

  List<TransactionEntity> getAllByContinuousTransactionIdAndDateIsLessThanEqual(
      String continuousTransactionId, LocalDate dateFrom);

  List<TransactionEntity> getAllByContinuousTransactionIdOrderByDate(
      String continuousTransactionId);

  List<TransactionEntity> getAllByBankAccountNullAndDateIsGreaterThanEqualAndDateIsLessThanEqual(
      LocalDate dateFrom, LocalDate dateUntil);

}
