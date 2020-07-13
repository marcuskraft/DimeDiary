package com.dimediary.rest.converter;

import com.dimediary.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionConverter {

  TransactionConverter INSTANCE = Mappers.getMapper(
      TransactionConverter.class);

  @Mapping(source = "subject", target = "name")
  Transaction to(com.dimediary.openapi.model.Transaction transaction);

  @Mapping(source = "name", target = "subject")
  com.dimediary.openapi.model.Transaction from(Transaction transaction);

}
