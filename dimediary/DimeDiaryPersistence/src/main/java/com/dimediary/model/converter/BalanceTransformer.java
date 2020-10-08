package com.dimediary.model.converter;

import com.dimediary.domain.Balance;
import com.dimediary.model.entities.BalanceEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BankAccountTransformer.class, UUIDTransformer.class})
public interface BalanceTransformer {

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  @Mapping(source = "balance.id", target = "id")
  BalanceEntity balanceHistoryToBalanceHistoryEntity(Balance balance,
      BankAccountEntity bankAccountEntityToUse);

  Balance balanceHistoryEntityToBalanceHistory(
      BalanceEntity balanceEntity);

}
