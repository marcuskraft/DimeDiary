package com.dimediary.rest.converter;

import com.dimediary.domain.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class,
    LocalDateConverter.class})
public interface BalanceRestConverter {

  @Mapping(target = "bankAccountName", source = "bankAccount.name")
  com.dimediary.openapi.model.Balance from(Balance balance);


}
