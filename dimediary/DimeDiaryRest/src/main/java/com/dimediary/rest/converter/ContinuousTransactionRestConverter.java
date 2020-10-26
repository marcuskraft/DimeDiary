package com.dimediary.rest.converter;

import com.dimediary.openapi.model.ContinuousTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class,
    CategoryRestConverter.class, LocalDateConverter.class,
    RecurrenceSettingsRecurrenceRuleRestConverter.class})
public interface ContinuousTransactionRestConverter {


  @Mapping(source = "recurrenceRule", target = "recurrenceSettings")
  ContinuousTransaction from(
      final com.dimediary.domain.ContinuousTransaction continuousTransaction);


  @Mapping(target = "timestamp", ignore = true)
  @Mapping(target = "recurrenceRule", source = "recurrenceSettings")
  com.dimediary.domain.ContinuousTransaction to(ContinuousTransaction continuousTransaction);

}
