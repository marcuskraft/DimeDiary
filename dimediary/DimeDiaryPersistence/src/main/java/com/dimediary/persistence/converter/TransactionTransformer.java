package com.dimediary.persistence.converter;

import com.dimediary.domain.Transaction;
import com.dimediary.persistence.entities.BankAccountEntity;
import com.dimediary.persistence.entities.CategoryEntity;
import com.dimediary.persistence.entities.ContinuousTransactionEntity;
import com.dimediary.persistence.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountTransformer.class, CategoryTransformer.class,
    ContinuousTransactionTransformer.class, UUIDTransformer.class})
public interface TransactionTransformer {

  @Mapping(source = "bankAccountEntity", target = "bankAccount")
  @Mapping(source = "continuousTransactionEntity", target = "continuousTransaction")
  @Mapping(source = "categoryEntity", target = "category")
  @Mapping(source = "transaction.amountEuroCent", target = "amountEuroCent")
  @Mapping(ignore = true, target = "timestamp")
  @Mapping(source = "transaction.id", target = "id")
  @Mapping(source = "transaction.name", target = "name")
  @Mapping(source = "transaction.fixCost", target = "fixCost")
  TransactionEntity transactionToTransactionEntity(Transaction transaction,
      BankAccountEntity bankAccountEntity,
      ContinuousTransactionEntity continuousTransactionEntity,
      CategoryEntity categoryEntity);

  Transaction transactionEntityToTransaction(
      TransactionEntity transaction);

}
