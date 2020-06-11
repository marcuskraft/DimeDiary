package com.dimediary.model.converter;

import com.dimediary.domain.Transaction;
import com.dimediary.model.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionTransformer {

  com.dimediary.model.converter.TransactionTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.TransactionTransformer.class);

  com.dimediary.model.entities.TransactionEntity transactionToTransactionEntity(
      Transaction transaction);

  @Mapping(source = "bankAccountEntity", target = "bankAccount")
  @Mapping(source = "continuousTransactionEntity", target = "continuousTransaction")
  @Mapping(source = "categoryEntity", target = "category")
  @Mapping(source = "transaction.amount", target = "amount")
  @Mapping(source = "transaction.timestamp", target = "timestamp")
  @Mapping(source = "transaction.id", target = "id")
  @Mapping(source = "transaction.name", target = "name")
  @Mapping(source = "transaction.fixCost", target = "fixCost")
  TransactionEntity transactionToTransactionEntity(Transaction transaction,
      com.dimediary.model.entities.BankAccountEntity bankAccountEntity,
      com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity,
      com.dimediary.model.entities.CategoryEntity categoryEntity);

  Transaction transactionEntityToTransaction(
      com.dimediary.model.entities.TransactionEntity transaction);

}
