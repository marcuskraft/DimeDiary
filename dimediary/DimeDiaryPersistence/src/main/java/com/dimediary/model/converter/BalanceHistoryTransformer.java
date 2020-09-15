package com.dimediary.model.converter;

import com.dimediary.domain.Balance;
import com.dimediary.model.entities.BalanceHistoryEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BankaccountTransformer.class)
public interface BalanceHistoryTransformer {

  @Mapping(target = "amount", source = "balance")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(
      Balance balance);

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  @Mapping(target = "amount", source = "balance.balance")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(Balance balance,
      BankAccountEntity bankAccountEntityToUse);

  @Mapping(target = "balance", source = "amount")
  Balance balanceHistoryEntityToBalanceHistory(
      BalanceHistoryEntity balanceHistoryEntity);

}
