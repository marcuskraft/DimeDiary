package com.dimediary.model.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContinuosTransactionTransformer {

  com.dimediary.model.converter.ContinuosTransactionTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.ContinuosTransactionTransformer.class);

  com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction);

  @Mapping(source = "bankAccountEntity", target = "bankAccount")
  @Mapping(source = "categoryEntity", target = "category")
  @Mapping(source = "continuousTransaction.name", target = "name")
  @Mapping(source = "continuousTransaction.fixCost", target = "fixCost")
  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction,
      com.dimediary.model.entities.BankAccountEntity bankAccountEntity,
      com.dimediary.model.entities.CategoryEntity categoryEntity);

  ContinuousTransaction continuousTransactionEntityToContinuousTransaction(
      com.dimediary.model.entities.ContinuousTransactionEntity continuousTransactionEntity);

}
