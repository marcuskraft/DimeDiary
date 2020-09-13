package com.dimediary.rest.converter;

import com.dimediary.openapi.model.BalanceHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BankAccountRestConverter.class})
public interface BalanceHistoryRestConverter {

  BalanceHistory from(com.dimediary.domain.BalanceHistory balanceHistory);

  com.dimediary.domain.BalanceHistory to(BalanceHistory balanceHistory);

}
