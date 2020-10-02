package com.dimediary.rest.converter;

import com.dimediary.openapi.model.ContinuousTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class,
    CategoryRestConverter.class, LocalDateConverter.class})
public interface ContinuousTransactionRestConverter {

  ContinuousTransaction from(com.dimediary.domain.ContinuousTransaction continuousTransaction);

  @Mapping(target = "timestamp", ignore = true)
  com.dimediary.domain.ContinuousTransaction to(ContinuousTransaction continuousTransaction);

}
