package com.dimediary.rest.converter;

import com.dimediary.domain.Balance;
import com.dimediary.openapi.model.BalanceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class})
public interface BalanceRestConverter {

  @Mapping(target = "bankAccountName", source = "bankAccount.name")
  BalanceHistory from(Balance balance);


}
