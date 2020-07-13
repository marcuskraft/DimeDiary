package com.dimediary.model.converter;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.model.entities.BalanceHistoryEntity;
import com.dimediary.model.entities.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BalanceHistoryTransformer {


  BalanceHistoryTransformer INSTANCE = Mappers.getMapper(BalanceHistoryTransformer.class);

  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(
      BalanceHistory balanceHistory);

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(BalanceHistory balanceHistory,
      BankAccountEntity bankAccountEntityToUse);

  BalanceHistory balanceHistoryEntityToBalanceHistory(
      BalanceHistoryEntity balanceHistoryEntity);

}
