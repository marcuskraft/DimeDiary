package com.dimediary.rest.converter;

import com.dimediary.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ContinuousTransactionRestConverter.class,
    LocalDateTimeOffSetDateTimeConverter.class})
public interface TransactionRestConverter {


  @Mapping(source = "subject", target = "name")
  Transaction to(com.dimediary.openapi.model.Transaction transaction);

  @Mapping(source = "name", target = "subject")
  com.dimediary.openapi.model.Transaction from(Transaction transaction);


}
