package com.dimediary.persistence.converter;

import com.dimediary.domain.ContinuousTransaction;
import com.dimediary.persistence.entities.ContinuousTransactionEntity;
import com.dimediary.persistence.entities.RecurrenceExceptionEntity;
import com.dimediary.persistence.entities.RecurrenceExtraInstanceEntity;
import java.util.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountTransformer.class, CategoryTransformer.class,
    UUIDTransformer.class, RecurrenceRuleTransformer.class,
    RecurrenceExceptionsExtraTransformer.class})
public interface ContinuousTransactionTransformer {

  @Mapping(target = "exceptions", source = "exceptions")
  @Mapping(target = "extraInstances", source = "extraInstances")
  @Mapping(target = "recurrenceRule", source = "continuousTransaction.recurrenceRule")
  ContinuousTransactionEntity from(
      ContinuousTransaction continuousTransaction, Collection<RecurrenceExceptionEntity> exceptions,
      Collection<RecurrenceExtraInstanceEntity> extraInstances);

  @Mapping(target = "recurrenceExceptions", source = "exceptions")
  @Mapping(target = "recurrenceExtraInstances", source = "extraInstances")
  ContinuousTransaction to(final ContinuousTransactionEntity continuousTransactionEntity);

}
