package com.dimediary.model.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankaccountTransformer.class, CategoryTransformer.class})
public interface ContinuousTransactionTransformer {


  @Mapping(target = "dateBeginn", source = "dateBegin")
  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction);

  @Mapping(target = "dateBegin", source = "dateBeginn")
  ContinuousTransaction continuousTransactionEntityToContinuousTransaction(
      ContinuousTransactionEntity continuousTransactionEntity);

}
