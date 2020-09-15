package com.dimediary.model.converter;

import com.dimediary.domain.Balance;
import com.dimediary.model.entities.BalanceEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BankaccountTransformer.class)
public interface BalanceHistoryTransformer {

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  BalanceEntity balanceHistoryToBalanceHistoryEntity(Balance balance,
      BankAccountEntity bankAccountEntityToUse);
  
  Balance balanceHistoryEntityToBalanceHistory(
      BalanceEntity balanceEntity);

}
