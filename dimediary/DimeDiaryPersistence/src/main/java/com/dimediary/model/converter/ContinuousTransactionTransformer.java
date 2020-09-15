package com.dimediary.model.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BankaccountTransformer.class, CategoryTransformer.class})
public interface ContinuousTransactionTransformer {


  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction);

  ContinuousTransaction continuousTransactionEntityToContinuousTransaction(
      ContinuousTransactionEntity continuousTransactionEntity);

}
