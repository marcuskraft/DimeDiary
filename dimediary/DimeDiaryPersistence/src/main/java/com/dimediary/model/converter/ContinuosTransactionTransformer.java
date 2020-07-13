package com.dimediary.model.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.entities.BankAccountEntity;
import com.dimediary.model.entities.CategoryEntity;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContinuosTransactionTransformer {

  ContinuosTransactionTransformer INSTANCE = Mappers
      .getMapper(ContinuosTransactionTransformer.class);

  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction);

  @Mapping(source = "bankAccountEntity", target = "bankAccount")
  @Mapping(source = "categoryEntity", target = "category")
  @Mapping(source = "continuousTransaction.name", target = "name")
  @Mapping(source = "continuousTransaction.fixCost", target = "fixCost")
  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction, BankAccountEntity bankAccountEntity,
      CategoryEntity categoryEntity);

  ContinuousTransaction continuousTransactionEntityToContinuousTransaction(
      ContinuousTransactionEntity continuousTransactionEntity);

}
