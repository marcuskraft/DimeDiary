package com.dimediary.model.converter;

import com.dimediary.domain.BalanceHistory;
import com.dimediary.model.entities.BalanceHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BalanceHistoryTransformer {

  com.dimediary.model.converter.BalanceHistoryTransformer INSTANCE = Mappers.getMapper(
      com.dimediary.model.converter.BalanceHistoryTransformer.class);

  com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(
      BalanceHistory balanceHistory);

  @Mapping(source = "bankAccountEntityToUse", target = "bankAccount")
  BalanceHistoryEntity balanceHistoryToBalanceHistoryEntity(BalanceHistory balanceHistory,
      com.dimediary.model.entities.BankAccountEntity bankAccountEntityToUse);

  BalanceHistory balanceHistoryEntityToBalanceHistory(
      com.dimediary.model.entities.BalanceHistoryEntity balanceHistoryEntity);

}
