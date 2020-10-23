package com.dimediary.rest.converter;

import com.dimediary.openapi.model.ContinuousTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class,
    CategoryRestConverter.class, LocalDateConverter.class,
    RecurrenceSettingsRecurrenceRuleRestConverter.class})
public interface ContinuousTransactionRestConverter {

  ContinuousTransactionRestConverter INSTANCE = Mappers
      .getMapper(ContinuousTransactionRestConverter.class);

  @Named("toUse")
  static ContinuousTransaction from(
      final com.dimediary.domain.ContinuousTransaction continuousTransaction) {
    final ContinuousTransaction continuousTransactionRet = INSTANCE
        .fromWithoutRecurrenceRule(continuousTransaction);

    if (continuousTransactionRet == null) {
      return null;
    }

    continuousTransactionRet.setRecurrenceSettings(RecurrenceSettingsRecurrenceRuleRestConverter
        .from(continuousTransaction.getRecurrenceRule(),
            continuousTransaction.getRecurrenceExceptions(),
            continuousTransaction.getRecurrenceExtraInstances()));

    return continuousTransactionRet;
  }

  @Mapping(target = "timestamp", ignore = true)
  @Mapping(target = "recurrenceRule", source = "recurrenceSettings")
  @Mapping(target = "recurrenceExceptions", source = "recurrenceSettings.recurrenceExceptions")
  @Mapping(target = "recurrenceExtraInstances", source = "recurrenceSettings.recurrenceExtraInstances")
  com.dimediary.domain.ContinuousTransaction to(ContinuousTransaction continuousTransaction);

  @Mapping(target = "recurrenceSettings", ignore = true)
  ContinuousTransaction fromWithoutRecurrenceRule(
      final com.dimediary.domain.ContinuousTransaction continuousTransaction);

}
