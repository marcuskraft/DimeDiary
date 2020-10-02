package com.dimediary.rest.converter;

import com.dimediary.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ContinuousTransactionRestConverter.class,
    LocalDateTimeOffSetDateTimeConverter.class, LocalDateConverter.class})
public interface TransactionRestConverter {


  @Mapping(source = "subject", target = "name")
  @Mapping(target = "timestamp", ignore = true)
  Transaction to(com.dimediary.openapi.model.Transaction transaction);

  @Mapping(source = "name", target = "subject")
  com.dimediary.openapi.model.Transaction from(Transaction transaction);


}
