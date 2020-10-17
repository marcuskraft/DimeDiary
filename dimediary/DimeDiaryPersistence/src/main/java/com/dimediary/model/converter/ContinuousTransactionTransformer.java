package com.dimediary.model.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.model.entities.ContinuousTransactionEntity;
import com.dimediary.model.entities.RecurrenceExceptionEntity;
import com.dimediary.model.entities.RecurrenceExtraInstanceEntity;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountTransformer.class, CategoryTransformer.class,
    UUIDTransformer.class})
public interface ContinuousTransactionTransformer {

  @Mapping(target = "exceptions", source = "exceptions")
  @Mapping(target = "extraInstances", source = "extraInstances")
  ContinuousTransactionEntity continuousTransactionToContinuousTransactionEntity(
      ContinuousTransaction continuousTransaction, Collection<RecurrenceExceptionEntity> exceptions,
      Collection<RecurrenceExtraInstanceEntity> extraInstances);

  ContinuousTransaction continuousTransactionEntityToContinuousTransaction(
      ContinuousTransactionEntity continuousTransactionEntity);

}
