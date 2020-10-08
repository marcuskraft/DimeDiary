package com.dimediary.rest.converter;

import com.dimediary.domain.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class,
    LocalDateConverter.class})
public interface BalanceRestConverter {

  @Mapping(target = "bankAccountId", source = "bankAccount.id")
  com.dimediary.openapi.model.Balance from(Balance balance);


}
