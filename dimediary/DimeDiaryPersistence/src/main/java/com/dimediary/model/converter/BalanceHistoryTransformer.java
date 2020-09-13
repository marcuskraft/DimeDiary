package com.dimediary.model.converter;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.model.entities.BalanceHistoryEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BankaccountTransformer.class)
public interface BalanceHistoryTransformer {

  @Mapping(target = "amount", source = "balance")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(
      BalanceHistory balanceHistory);

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  @Mapping(target = "amount", source = "balanceHistory.balance")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(BalanceHistory balanceHistory,
      BankAccountEntity bankAccountEntityToUse);

  @Mapping(target = "balance", source = "amount")
  BalanceHistory balanceHistoryEntityToBalanceHistory(
      BalanceHistoryEntity balanceHistoryEntity);

}
